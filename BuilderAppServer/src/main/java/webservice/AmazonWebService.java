package main.java.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

import main.java.global.AppConstants;
import main.java.objects.ItemSearchExtract;
/**
 * Amazon Web Service calls
 * Customized to get computer parts
 * @author Peter
 *
 */
public class AmazonWebService {

    private static String AWS_ACCESS_KEY_ID = "";
    private static String AWS_SECRET_KEY = "";
    private static final String ENDPOINT = "webservices.amazon.com";
    private static boolean AWSGoodStatus = false;
    private static final int maxNumPages = 10;
    private static final int maxRetryCount = 5;
    private static int retryCount = 0;
    //Back off time to wait
    private static final int backOffTime = 3000;
    //Help narrow the search and fetch more valid computer part items
    private static final Map<String, String> browseNodes;
    static{
    	browseNodes = new HashMap<String, String>();
    	browseNodes.put(AppConstants.cpu, "229189");
    	browseNodes.put(AppConstants.gpu, "284822");
    	browseNodes.put(AppConstants.mobo, "1048424");
    	browseNodes.put(AppConstants.memory, "172500");
    	browseNodes.put(AppConstants.psu, "1161760");
    	browseNodes.put(AppConstants.disk, "1254762011");
    	browseNodes.put("DEFAULT", "193870011");
    }
    
    /**
     * Set AWS Credentials (should only be called once)
     */
    private static void initAccess(){		
    	try {
    		//Plan A use Default Credentials Provider attached to EC2 instance
    		AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
            AWS_ACCESS_KEY_ID = credentialsProvider.getCredentials().getAWSAccessKeyId();
            AWS_SECRET_KEY = credentialsProvider.getCredentials().getAWSSecretKey();
            //Plan B read from file or path
            AWSGoodStatus = true;
            System.out.println("Good AWS Credentials");
        } catch (Exception e) {
            System.err.println("AWS not enabled, Exception: " + e.getMessage());
            AWS_ACCESS_KEY_ID = "FAIL";
            AWS_SECRET_KEY = "FAIL";
            AWSGoodStatus = false;
            System.out.println("Bad AWS Credentials");
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
	        retryCount = 0;
	        itemSearch.addAll(fetchInformation(requestURL));
    	}        
        
        return itemSearch;
    }
    
    /**
     * Call to get specific/highly relevant item(s). Makes only one request (quick). Returns up to 10
     * @return
     */
    public static ArrayList<ItemSearchExtract> getSpecificItems(String maxPrice, String minPrice,
    		String keywords, String computerPartType){
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
	        
	    requestURL = createSignedRequest(params);
	    retryCount = 0;
	    itemSearch = fetchInformation(requestURL);    
        
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
     * @return null if something bad happened
     */
    private static ArrayList<ItemSearchExtract> fetchInformation(String requestUrl) {
    	ArrayList<ItemSearchExtract> prodList = new ArrayList<ItemSearchExtract>();
    	ItemSearchExtract temp;
        Node aNode;
        
        if(requestUrl == null){
    		return prodList;
    	}
        
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
	            		temp.itemPicURL = ((Element) imgNode).getElementsByTagName("URL").item(0) == null ? null : 
	            			((Element) imgNode).getElementsByTagName("URL").item(0).getTextContent();
	            	}
	            	//Product Name/Title
	            	Node itemAttr = ((Element) aNode).getElementsByTagName("ItemAttributes").item(0);
	            	if(itemAttr != null){
	            		temp.itemName = ((Element) itemAttr).getElementsByTagName("Title").item(0) == null ? null :
	            			((Element) itemAttr).getElementsByTagName("Title").item(0).getTextContent();
	            	}
	            	//Product ID (Computer Part ID)
	            	temp.itemPartID = ((Element) aNode).getElementsByTagName("PartNumber").item(0) == null ? null :
	            			((Element) aNode).getElementsByTagName("PartNumber").item(0).getTextContent();
	            	//Product Price
	            	Float newPrice, usedPrice;
	            	Node offerSumm = ((Element) aNode).getElementsByTagName("OfferSummary").item(0);
	            	Node newPriceNodes = ((Element) offerSumm).getElementsByTagName("LowestNewPrice").item(0);
	            	Node usedPriceNodes = ((Element) offerSumm).getElementsByTagName("LowestUsedPrice").item(0);
	            	if(newPriceNodes == null){
	            		newPrice = Float.MAX_VALUE;
	            	}
	            	else{
	            		newPrice = ((Element) newPriceNodes).getElementsByTagName("Amount").item(0) == null ? Float.MAX_VALUE :
	            			Float.parseFloat(((Element) newPriceNodes).getElementsByTagName("Amount").item(0).getTextContent());
	            	}
	            	if(usedPriceNodes == null){
	            		usedPrice = Float.MAX_VALUE;
	            	}
	            	else{
	            		usedPrice = ((Element) usedPriceNodes).getElementsByTagName("Amount").item(0) == null ? Float.MAX_VALUE :
	            			Float.parseFloat(((Element) usedPriceNodes).getElementsByTagName("Amount").item(0).getTextContent());
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
            		System.out.println("Some error when parsing out some element/node in XML: " + e);
            		//return null;
            	}
            }
        } catch (Exception e) {
        	if(e.getMessage().contains("HTTP response code: 503") && retryCount < maxRetryCount){
        		try {
        			//Backoff
        			System.err.println("Probably hitting Amazon too fast... waiting " + backOffTime/1000 + " sec");
        			Thread.sleep(backOffTime);
        			retryCount++;
        			return fetchInformation(requestUrl);
    			} catch (InterruptedException intE) {
    				//exit early
    				return prodList;
    			}
        	}
        	else{
        		System.err.println("Exception encountered: " + e.getMessage());
        	}
        	return null;
        }
    	return prodList;
    }
}
