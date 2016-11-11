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

import main.java.objects.ItemSearchExtract;

/*
 * This class shows how to make a simple authenticated call to the
 * Amazon Product Advertising API.
 *
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class AmazonWebService {

    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAJJFSS6TWRQKKFPEQ";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "feiWFkSTJJEXpSFFQwwEHKNoKVaH68w2C8a+Mm5O";

    /*
     * Use the end-point according to the region you are interested in.
     */
    private static final String ENDPOINT = "webservices.amazon.com";

    public static void main(String[] args) {

        /*
         * Set up the signed requests helper.
         */
        SignedRequestsHelper helper;

        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String requestUrl = null;

        Map<String, String> params = new HashMap<String, String>();

        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("AWSAccessKeyId", "AKIAJJFSS6TWRQKKFPEQ");
        params.put("AssociateTag", "autocompbuild-20");
        params.put("SearchIndex", "Electronics");
        params.put("Keywords", "intel cpu");
        params.put("ResponseGroup", "ItemAttributes,OfferSummary,Images");
        params.put("Sort", "-price");
        params.put("MaximumPrice", "100000");
        params.put("MinimumPrice", "5000");
        params.put("ItemPage", "2");

        requestUrl = helper.sign(params);
        
        System.out.println("Signed URL: \"" + requestUrl + "\"");
        
        ArrayList<ItemSearchExtract> sample = fetchInformation(requestUrl);
        for(ItemSearchExtract item : sample){
        	System.out.println(item.toString());
        }
    }
    
    /*
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
        }
    	return prodList;
    }
}
