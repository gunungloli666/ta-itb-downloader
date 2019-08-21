package fjr.com.extract;

import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Test3 {
	
	public static void main(String[] args) {
		  final WebClient webClient = new WebClient();

		    // Get the first page
		    HtmlPage page1;
			try {
				page1 = webClient.getPage("http://defasites.appspot.com/contact.jsp");
			
		    // Get the form that we are dealing with and within that form, 
		    // find the submit button and the field that we want to change.
		    List< HtmlForm> listform = page1.getForms();
		    if( listform.size() >= 1) {
		    	HtmlForm form = listform.get(0); 
			    HtmlTextInput inputName = form.getInputByName("fullname");
			    HtmlTextInput email = form.getInputByName("email"); 
			    HtmlTextInput phone = form.getInputByName("phone") ;
			    HtmlTextArea message = form.getTextAreaByName("message");  
			    
			    inputName.setValueAttribute("ardi"); 
			    email.setValueAttribute("fitria"); 
			    phone.setValueAttribute("1234");
			    message.setText("halo pak dedi...... test spam yaaa" ); 
			    
			    HtmlElement element =  (HtmlElement) page1.createElement("button");
			    element.setAttribute("type" , "submit");
			    form.appendChild(element);
			    
			    
//			    button.setAttribute("type", "submit");
			    
			    HtmlPage page2 = element.click();
			    String xml = page2.asXml(); 
			    
			    System.out.println(xml); 
//			    
		    }
		    
			} catch (FailingHttpStatusCodeException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
