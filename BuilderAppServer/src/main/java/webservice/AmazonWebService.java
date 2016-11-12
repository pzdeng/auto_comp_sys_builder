package main.java.webservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import main.java.global.AppConstants;
import main.java.objects.ItemSearchExtract;

public class AmazonWebService {

    private static String AWS_ACCESS_KEY_ID = "";
    private static String AWS_SECRET_KEY = "";
    private static final String ENDPOINT = "webservices.amazon.com";
    private static boolean AWSGoodStatus = false;
    private static final int maxNumPages = 10;
    private static final Map<String, String> browseNodes;
    static{
    	browseNodes = new HashMap<String, String>();
    	browseNodes.put(AppConstants.cpu, "229189");
    	browseNodes.put(AppConstants.gpu, "284822");
    	browseNodes.put(AppConstants.mobo, "1048424");
    	browseNodes.put("DEFAULT", "193870011");
    }
    
    /**
     * Set AWS Credentials (should only be called once)
     */
    private static void initAccess(){
    	String fileloc = new String("datasourceExtract" + File.separator + "aws.csv");
    	BufferedReader br = null;
		String[][] temp = new String[2][];
		
    	try {
            br = new BufferedReader(new FileReader(fileloc));
            temp[0] = br.readLine().split("::");
            temp[1] = br.readLine().split("::");
            AWS_ACCESS_KEY_ID = temp[0][1];
            AWS_SECRET_KEY = temp[1][1];
            AWSGoodStatus = true;
        } catch (IOException e) {
            System.err.println("AWS not enabled, Exception: " + e.getMessage());
            AWS_ACCESS_KEY_ID = "FAIL";
            AWS_SECRET_KEY = "FAIL";
            AWSGoodStatus = false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * General call to get up to 100 relevant items (potentially long running call)
     * @return
     */
    public static ArrayList<ItemSearchExtract> getGeneralItems(String maxPrice, String minPrice, 
    		int pageLimit, String keywords, String computerPartType){
    	Map<String, String> params = paramsSetUp();
    	String requestURL;
    	ArrayList<ItemSearchExtract> itemSearch = new ArrayList<ItemSearchExtract>();
    	
    	//Variable additional parameters
    	String browseNodeVal = browseNodes.get(computerPartType) != null ? 
    			browseNodes.get(computerPartType) : browseNodes.get("DEFAULT");
        params.put("BrowseNode", browseNodeVal);
        params.put("Keywords", keywords);
        params.put("MaximumPrice", maxPrice);
        params.put("MinimumPrice", minPrice);
        
        pageLimit = maxNumPages > pageLimit ? pageLimit : maxNumPages;
    	for(int i = 0; i < maxNumPages; i++){	
	        params.put("ItemPage", i + 1 + "");
	        
	        requestURL = createSignedRequest(params);
	        itemSearch.addAll(fetchInformation(requestURL));
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Got interrupt, ending ItemSearch early.");
				return itemSearch;
			}
    	}        
        
        return itemSearch;
    }
    
    /**
     * Call to get specific/highly relevant item(s). Makes only one request (quick). Returns up to 10
     * @return
     */
    public ArrayList<ItemSearchExtract> getSpecificItems(String maxPrice, String minPrice, 
    		String page, String keywords, String computerPartType){
    	Map<String, String> params = paramsSetUp();
    	String requestURL;
    	ArrayList<ItemSearchExtract> itemSearch = new ArrayList<ItemSearchExtract>();
    	
    	//Variable additional parameters
    	String browseNodeVal = browseNodes.get(computerPartType) != null ? 
    			browseNodes.get(computerPartType) : browseNodes.get("DEFAULT");
    	params.put("BrowseNode", browseNodeVal);
	    params.put("Keywords", keywords);
	    params.put("MaximumPrice", maxPrice);
        params.put("MinimumPrice", minPrice);
	    params.put("ItemPage", "1");
	        
	    requestURL = createSignedRequest(params);
	    itemSearch.addAll(fetchInformation(requestURL));    
        
        return itemSearch;
    }
    
    /**
     * Initialize parameter mapping object to feed into ItemSearch web service call
     * @param maxPrice
     * @param minPrice
     * @param page
     * @param keywords
     * @return
     */
    private static Map<String, String> paramsSetUp(){
    	Map<String, String> params = new HashMap<String, String>();
    	
    	//Static parameters
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("AssociateTag", "autocompbuild-20");
        params.put("SearchIndex", "Electronics");
        params.put("ResponseGroup", "ItemAttributes,OfferSummary,Images");
        params.put("Sort", "salesrank");
        
        return params;
    }
    
    /**
     * Gets the signed request url to perform ItemSearch
     * @param params
     * @return
     */
    private static String createSignedRequest(Map<String, String> params){
    	if(AWS_ACCESS_KEY_ID.length() == 0){
    		initAccess();
    	}
    	else if(!AWSGoodStatus){
    		//AWS credential no good
    		return null;
    	}
    	
        SignedRequestsHelper helper;
        String requestURL = null;
        
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
            requestURL = helper.sign(params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    	return requestURL;
    }
    
    /**
     * Utility function to fetch the response from the service and extract product information from ItemSearch service
     * Price, URL, PicURL
     */
    private static ArrayList<ItemSearchExtract> fetchInformation(String requestUrl) {
    	ArrayList<ItemSearchExtract> prodList = new ArrayList<ItemSearchExtract>();
    	ItemSearchExtract temp;
        Node aNode;
    	try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            //XML Breakdown
            //Root: ItemSearchResponse
            //Node: Items
            //NodeList: Item
            Node itemList = doc.getElementsByTagName("Items").item(0);
            NodeList nodeList = ((Element) itemList).getElementsByTagName("Item");
            for(int i = 0; i < nodeList.getLength(); i++){
            	try{
	            	aNode = nodeList.item(i);
	            	temp = new ItemSearchExtract();
	            	//Page URL
	            	temp.itemURL = ((Element) aNode).getElementsByTagName("DetailPageURL").item(0).getTextContent();
	            	//Medium Picture URL
	            	Node imgNode = ((Element) aNode).getElementsByTagName("MediumImage").item(0);
	            	if(imgNode == null){
	            		//Try small image
	            		imgNode = ((Element) aNode).getElementsByTagName("SmallImage").item(0);
	            	}
	            	if(imgNode != null){
	            		temp.itemPicURL = ((Element) imgNode).getElementsByTagName("URL").item(0).getTextContent();
	            	}
	            	//Product Name/Title
	            	Node itemAttr = ((Element) aNode).getElementsByTagName("ItemAttributes").item(0);
	            	temp.itemName = ((Element) itemAttr).getElementsByTagName("Title").item(0).getTextContent();
	            	//Product ID (Computer Part ID)
	            	temp.itemPartID = ((Element) aNode).getElementsByTagName("PartNumber").item(0).getTextContent();
	            	//Product Price
	            	Float newPrice, usedPrice;
	            	Node offerSumm = ((Element) aNode).getElementsByTagName("OfferSummary").item(0);
	            	Node newPriceNode = ((Element) offerSumm).getElementsByTagName("LowestNewPrice").item(0);
	            	Node usedPriceNode = ((Element) offerSumm).getElementsByTagName("LowestUsedPrice").item(0);
	            	if(newPriceNode == null){
	            		newPrice = Float.MAX_VALUE;
	            	}
	            	else{
	            		newPrice = Float.parseFloat(((Element) newPriceNode).getElementsByTagName("Amount").item(0).getTextContent());
	            	}
	            	if(usedPriceNode == null){
	            		usedPrice = Float.MAX_VALUE;
	            	}
	            	else{
	            		usedPrice = Float.parseFloat(((Element) usedPriceNode).getElementsByTagName("Amount").item(0).getTextContent());
	            	}
	            	temp.itemPrice = newPrice > usedPrice ? usedPrice : newPrice;
	            	//Check if price == Float.MAX_VALUE
	            	if(temp.itemPrice == Float.MAX_VALUE){
	            		//Set to zero
	            		temp.itemPrice = 0;
	            	}
	            	//Transform amount to dollars (Amazon reports using lowest denomination, pennies)
	            	temp.itemPrice /= 100;
	            	prodList.add(temp);
            	} catch (Exception e){
            		System.out.println("Some error when parsing out some element/node in XML: " + e.getMessage());
            	}
            }
        } catch (Exception e) {
        	System.err.println("Exception encountered: " + e.getMessage());
        	/*
        	if(e.getMessage().contains("HTTP response code: 503")){
        		try {
        			//Backoff
    				Thread.sleep(1000);
    			} catch (InterruptedException intE) {
    				//exit early
    				return prodList;
    			}
        	}
        	*/
        }
    	return prodList;
    }
}
