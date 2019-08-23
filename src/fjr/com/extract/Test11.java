package fjr.com.extract;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Test11 {

	PrintWriter out2 = null ; 
	WebClient webClient; 
	String dir = "./TA-ITB/" ;
	public Test11() {
			
		  webClient = new WebClient();
		    HtmlPage page1;
			try {
				File f = new File(dir); 
				dir = f.getAbsolutePath();
				if(f.exists()) {
					FileUtils.deleteDirectory(f);
				}
				f.mkdir();
				out2 = new PrintWriter(new FileOutputStream(dir + "/" + "hasil_2.txt"));

				
				webClient.getOptions().setJavaScriptEnabled(false);
				webClient.getOptions().setCssEnabled(false);
				
				System.out.println("mulai"); 
				
				int pagenumber = 0; 
				
				out2.println("<html><body>");
				while(true) {
					String topage= Integer.toString(pagenumber);
					if(pagenumber == 0) {
						topage = ""; 
					}
					String pagetToOpen = "https://digilib.itb.ac.id/index.php/collection/type/7/" + topage ;
					page1 = webClient.getPage( pagetToOpen);
					List<DomElement> listUl =  page1.getElementsByTagName("ul"); 
				
					if( listUl.size() > 0) {
						for(DomElement el : listUl) {
							HtmlElement element = (HtmlElement) el; 
							int numChild = element.getChildElementCount(); 
							if( numChild == 10) {
								System.out.println("start to process: " + pagetToOpen); 
								Iterable<DomElement> it = element.getChildElements(); 
								Iterator<DomElement> iterator = it.iterator();
								while( iterator.hasNext()) {
									HtmlElement child = (HtmlElement) iterator.next(); 
									DomNodeList<HtmlElement> list2 =  child.getElementsByTagName("div"); 
									if( list2.size() >1) {
										HtmlElement elem = list2.get(1); 
										DomNodeList<HtmlElement > ll = elem.getElementsByTagName("a"); 
										DomNodeList<HtmlElement> l1 = elem.getElementsByTagName("sub");
										String textpengarang  = ""; 
										if(l1.size() > 0) {
											textpengarang = l1.get(0).asText();
										}
										if(ll.size() > 0) {
											HtmlElement link = ll.get(0);
											String tt = link.asText();
											out2.println("<strong>"+tt + "<br />");
											out2.println(textpengarang + "</strong><br />");
											
											String s = link.getAttribute("href"); 
//											out2.println(s)
											openInNewWindow(s);				
											out2.println("<br />");
											
											out2.flush();
										}
									}
									
								}
							}

						}

					}else {
						break;
					}
					
					pagenumber+= 10;

				}
				System.out.println("finish"); 
			} catch (FailingHttpStatusCodeException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(out2 != null ) {
					out2.println("</body>");
					out2.println("</html>");
					out2.close();
				}
			}


	}
	
	
	public static void main(String[] args) {
		new Test11();
	}
	
	
	// buka halaman untuk satu pengarang
	void openInNewWindow(String link) {
		try {
			HtmlPage page = webClient.getPage(link);
			printStrongElement(page);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void printStrongElement(HtmlPage page) throws Exception{
		DomNodeList<DomElement> listelement = page.getElementsByTagName("strong"); 
		Iterator<DomElement> iterator = listelement.iterator();
		while( iterator.hasNext()) {
			HtmlElement elem = (HtmlElement) iterator.next();
			DomNodeList<HtmlElement> listelm1 = elem.getElementsByTagName("a"); 
			if(listelm1.size() > 0) {
				HtmlElement link = (HtmlElement)listelm1.get(0);
				String s = link.getAttribute("href"); 
				openTa(s);
				out2.println(link.asText()+"</a><br />"); 
			}
		}
	}
	
	public void openTa(String link ) {
		try {
			HtmlPage page = webClient.getPage(link );
			DomElement element =  page.getElementById("viewer");
			HtmlElement elem = (HtmlElement) element; 
			String s = elem.getAttribute("data-url"); 
			out2.print("<a href=\"" + s +"\">"); 
//			out2.flush();
		}catch(Exception e) {
			
		}
	}
	

}
