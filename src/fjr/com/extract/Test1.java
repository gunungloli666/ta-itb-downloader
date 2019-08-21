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

public class Test1 {

	PrintWriter out2, out1 , out3; 
	WebClient webClient; 
	String dir = "D:/TA ITB/" ;
	public Test1() {
		
		  webClient = new WebClient();
		    HtmlPage page1;
			try {
				
				out2 = new PrintWriter(new FileOutputStream(dir + "hasil_2.txt"));
				out1 = new PrintWriter(new FileOutputStream(dir + "hasil_1.txt"));
				out3 = new PrintWriter(new FileOutputStream(dir + "hasil_3.txt"));

				
				webClient.getOptions().setJavaScriptEnabled(false);
				webClient.getOptions().setCssEnabled(false);
				
				int pagenumber = 0; 
				int index = 0; 
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
										if(ll.size() > 0) {
											HtmlElement link = ll.get(0);
											String s = link.getAttribute("href"); 
											String hasil = openInNewWindow(s, Integer.toString(++index));
											out2.println(hasil); 
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
				out2.close();
				out1.close();
				out3.close(); 
				System.out.println("finish"); 
			} catch (FailingHttpStatusCodeException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}
	
	
	public static void main(String[] args) {
		new Test1();
	}
	
	
	// buka halaman untuk satu pengarang
	 String openInNewWindow(String link, String root) {
		try {
			HtmlPage page = webClient.getPage(link);
			String rootName = dir + "/" + root;
			File f = new File(rootName); 
			if(f.exists()) {
				FileUtils.deleteDirectory(f);
			}
			f.mkdir();
			out3.println("<=======>");
			printStrongElement(page, rootName);
//			System.out.println(page.asXml()); 
//			System.out.println("=========");
			StringBuilder builder = new StringBuilder(); 
			builder.append(page.asXml()); 
			builder.append("=========="); 
			return builder.toString();
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
		return "+++++"; 
	}
	
	public  void printStrongElement(HtmlPage page, String dir) throws Exception{
		DomNodeList<DomElement> listelement = page.getElementsByTagName("strong"); 
		Iterator<DomElement> iterator = listelement.iterator();
		int index = 0; 
		while( iterator.hasNext()) {
			HtmlElement elem = (HtmlElement) iterator.next();
//			System.out.println(elem.getTextContent());
			DomNodeList<HtmlElement> listelm1 = elem.getElementsByTagName("a"); 
			if(listelm1.size() > 0) {
				HtmlElement link = (HtmlElement)listelm1.get(0);
				String s = link.getAttribute("href"); 
//				System.out.println(s + "|" + link.asText()); 
				out1.println(s + "|" + link.asText()); 
				openTa(s, dir+ "/" + Integer.toString(++index) + ".pdf");
				out3.println("------");
			}
		}
	}
	
	public void openTa(String link , String file) {
		try {
			HtmlPage page = webClient.getPage(link );
			DomElement element =  page.getElementById("viewer");
			HtmlElement elem = (HtmlElement) element; 
			String s = elem.getAttribute("data-url"); 
			out3.println(s); 
			downloadToDir(s , file);
		}catch(Exception e) {
			
		}
	}
	

	public void downloadToDir(String link, String dir) {
		
		try ( BufferedInputStream inputStream = new BufferedInputStream(
				new URL(link).openStream());
				FileOutputStream fileOS = new FileOutputStream(dir)) {
			byte data[] = new byte[1024];
			int byteContent;
			while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
				fileOS.write(data, 0, byteContent);
			}
		} catch (IOException e) {
			// handles IO exceptions
		}
	}

}
