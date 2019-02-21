package in.zipgo.ZipgoKnowlarityData;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import org.testng.util.RetryAnalyzerCount;

import jxl.read.biff.BiffException;
import library.Utility;

public class KnowlaritycombinedReport {

	final By userid1 			=By.xpath("//*[@id='dbform']/div/div[1]/input");
	final By pwd1 				=By.xpath("//*[@id='dbform']/div/div[2]/input");
	final By userid2 			=By.xpath("//*[@id='dbform']/div/div[1]/input");
	final By pwd2 				=By.xpath("//*[@id='dbform']/div/div[2]/input");
	final By clickOnDbForm 		=By.xpath("//*[@id='dbform']/div/div[5]/input");
	final By queryField 		=By.xpath("//body[@class='editbox']");
	final By submitQuery 		=By.xpath("//span[text()='Query']");
	final By logout 			=By.xpath("//a[@class='ilgout']");
	final By logoutConf 		=By.xpath("//*[@id='popup_ok']/span");

	String FileLocation;
	String FileName[] 			={ "zipgo_ibd_call_logs_info-results.csv", "zipgo_t_15145_call_logs_view-results.csv" };
	String Url					="https://etsrds.knowlarity.com/";
	String mailid 				="prashant.zipgouser@gmail.com";
	String mailpwd 				="Zipgouser@321";
	String LoginID[] 			={ "zipgo_ibd", "zipgo_t" };
	String ibd 					="zipgo_ibd";
	String driveribd 			="zipgo_t";
	String pwd1value			="zipgo@2017";
	String pwd2value			="zipgo_t@2017";
	String sqlEditFrame			="sqlEditFrame";
	String dilogDataFrame		="dialog-data-export-contents";

	String Query;
	String dateFinal;
	String NoDataMsg;
	String mailMsg;
	String ScrName;
	

	@Test(priority = 1, invocationCount=1,enabled=true, retryAnalyzer = RetryAnalyzerCount.class)
	public void deleteOldFile() {
		System.out.println("deleteOldFile process started...");
		
//---below two lines will get universal file location independent from OS---
		
		String home = System.getProperty("user.home");
		FileLocation = home+ File.separator + "Downloads"+ File.separator;
		System.out.println(FileLocation);
//===========================================================================		
		

		for (String fileName : FileName) {

			try {

				File file = new File(FileLocation + fileName);

				if (file.delete()) {

					System.out.println(file.getName() + " is deleted!");

				} else {

					System.out.println("Delete operation is failed.");

				}

				System.out.println("File deleted  sucessfully");
			} catch (Exception e1) {

				e1.printStackTrace();

				System.out.println("File has not avilable on location or deleted Allready ");
			}
		}

	}

	@Test(priority = 2, invocationCount = 1, enabled = true, retryAnalyzer = RetryAnalyzerCount.class)
	public void downloadData() throws IOException, InterruptedException, BiffException {

//		===============Date Picker================================
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		dateFinal = df.format(cal.getTime());
//		dateFinal  = "30-11-2018";
//	    ===========================================================

		System.out.println("Downloading Data... ");
//		===========================================================Entering URL=============
		WebDriver driver = new ChromeDriver();
		try {
			driver.get(Url);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("Unable to process...Please check internet connection or url");
			e1.printStackTrace();
		}
		
//		==========================================================================================
		for (String loginid : LoginID) {

			if (loginid.equalsIgnoreCase(ibd)){
				driver.findElement(userid1).sendKeys(loginid);
				driver.findElement(pwd1).sendKeys(pwd1value);
				Query = "select * from zipgo_ibd_call_logs_info where call_date ='";
				ScrName=loginid;
			}

			if (loginid.equalsIgnoreCase(driveribd)) {
				driver.findElement(userid2).sendKeys(loginid);
				driver.findElement(pwd2).sendKeys(pwd2value);
				Query = "select * from zipgo_t_15145_call_logs_view where call_date ='";
				ScrName=loginid;
			}

			driver.findElement(clickOnDbForm).click();
			Thread.sleep(1000);
			driver.switchTo().frame(sqlEditFrame);
			Thread.sleep(1000);
			driver.findElement(queryField).clear();
			driver.findElement(queryField).sendKeys(Query + dateFinal + "'");
			Thread.sleep(2000);
			driver.switchTo().defaultContent();
			Thread.sleep(2000);
			driver.findElement(submitQuery).click();
			/*if (!driver.findElement(By.xpath("(//input[@class='check-all'])[2]")).isSelected()) {
				driver.findElement(By.xpath("(//input[@class='check-all'])[2]")).click();
			}*/
			driver.findElement(By.xpath("//a[text()='Data']")).click();
			driver.findElement(By.xpath("//a[text()='Data']")).click();
			driver.findElement(By.xpath("//a[@title='Export query results to clipboard or files']")).click();
			Thread.sleep(2000);
			try {
				driver.switchTo().frame(dilogDataFrame);
				Thread.sleep(2000);
				WebElement z = driver.findElement(By.xpath("//*[@id='csv']"));
				((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", z);
				WebElement e = driver.findElement(By.xpath("//*[@id='btn_export']"));
				Thread.sleep(2000);
				e.click();
				Utility.getScreenshot(driver,dateFinal+ScrName);  // Take screen shot
			} catch (Exception e) {
				// TODO Auto-generated catch block
				NoDataMsg = "There is no record in the results to export";
				Utility.getScreenshot(driver,dateFinal+ScrName);  // Take screen shot
				System.out.println(NoDataMsg+" with query>>"+Query);
				e.printStackTrace();
			}
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			driver.navigate().refresh();
			Thread.sleep(2000);
			driver.findElement(logout).click();
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			driver.findElement(logoutConf).click();
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			
		}

		
		driver.quit();
	}

	@Test(priority = 4, invocationCount = 1, enabled = true, retryAnalyzer = RetryAnalyzerCount.class)
	public void IBDCallLogsDumpMail() throws EmailException, MalformedURLException {
		String mailSubject 		="IBD Call Logs Dump | ";
		String scrlocation		="./screenshot/"+dateFinal+"zipgo_ibd"+".png";

		System.out.println("Sending attachement mail..");

		// Create the attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(FileLocation+"zipgo_ibd_call_logs_info-results.csv");
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setName("zipgo_ibd_call_logs_info-results" + dateFinal + ".csv");

//	==============
		// Create the attachment
		EmailAttachment attachment1 = new EmailAttachment();
		attachment1.setPath(scrlocation);
		attachment1.setDisposition(EmailAttachment.ATTACHMENT);
		attachment1.setName("zipgo_ibd" + dateFinal + ".png");

//	====================

		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.gmail.com");

		email.setSmtpPort(465);

		email.setAuthenticator(new DefaultAuthenticator(mailid, mailpwd));

		email.setSSLOnConnect(true);
		email.setFrom(mailid);

		email.setSubject(mailSubject + dateFinal);

//		email.addTo("prashant.ranjan@zipgo.in");

		email.addTo("aditya.kesarkar@zipgo.in");
		email.addTo("arpit@zipgo.in");
		email.addTo("rahul.roy@zipgo.in");
		email.addTo("cs-quality@zipgo.in");
		email.addTo("palak@zipgo.in");

		email.addReplyTo("sandesh@zipgo.in");

		email.addCc("sandesh@zipgo.in");
		email.addCc("gaurav@zipgo.in");

		email.addCc("prashant.ranjan@zipgo.in");

		// add the attachment
		try {
			email.attach(attachment);
			email.attach(attachment1);
			mailMsg="Please find attached of ";
		} catch (Exception e) {
			email.attach(attachment1);
			// TODO Auto-generated catch block
			System.out.println(mailMsg);
			
			mailMsg="There is no record in the results to export of ";
			e.printStackTrace();
		}
		email.setMsg(mailMsg + dateFinal);
		// send the email
		email.send();
		System.out.println("Sent"+mailSubject);

	}

	@Test(priority = 5, invocationCount = 1, enabled = true)
	public void DriverSupportIBDCallLogsDumpMail() throws EmailException, MalformedURLException {
		String mailSubject 		="Driver Support IBD Call Logs Dump | ";
		String scrlocation		="./screenshot/"+dateFinal+ScrName+".png";

		System.out.println("Sending attachement mail..");
		// Create the attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(FileLocation+"zipgo_t_15145_call_logs_view-results.csv");
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setName("zipgo_t_15145_call_logs_view-results" + dateFinal + ".csv");
		
//	==============
		// Create the attachment
		EmailAttachment attachment1 = new EmailAttachment();
		attachment1.setPath(scrlocation);
		attachment1.setDisposition(EmailAttachment.ATTACHMENT);
		attachment1.setName(ScrName+ dateFinal+".png");

//	====================

		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.gmail.com");

		email.setSmtpPort(465);

		email.setAuthenticator(new DefaultAuthenticator(mailid, mailpwd));

		email.setSSLOnConnect(true);
		email.setFrom(mailid);

		email.setSubject(mailSubject + dateFinal);

//		email.addTo("prashant.ranjan@zipgo.in");

		email.addTo("prashant.suman@zipgo.in");
		email.addTo("chandan@zipgo.in");
		email.addTo("mohit.chabbra@zipgo.in");
		email.addTo("arpit@zipgo.in");
		email.addTo("aditya.kesarkar@zipgo.in");
		email.addTo("palak@zipgo.in");

		email.addReplyTo("sandesh@zipgo.in");

		email.addCc("sandesh@zipgo.in");
		email.addCc("gaurav@zipgo.in");

		email.addCc("prashant.ranjan@zipgo.in");

		email.setMsg("Please find attached of " + dateFinal);
		// add the attachment
				try {
					email.attach(attachment);
					email.attach(attachment1);
					mailMsg="Please find attached of ";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(mailMsg);
					email.attach(attachment1);
					mailMsg="There is no record in the results to export of ";
					e.printStackTrace();
				}
				email.setMsg(mailMsg + dateFinal);
				// send the email
				email.send();
				System.out.println("Sent"+mailSubject);

	}
}
