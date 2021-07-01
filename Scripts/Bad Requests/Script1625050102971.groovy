import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.Keys as Keys

import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.util.CDTUtils
import com.kms.katalon.core.util.internal.Base64

import com.github.kklisura.cdt.protocol.commands.Fetch
import com.github.kklisura.cdt.protocol.commands.Network
import com.github.kklisura.cdt.protocol.commands.Page
import com.github.kklisura.cdt.protocol.types.network.ErrorReason
import com.github.kklisura.cdt.services.ChromeDevToolsService
import com.github.kklisura.cdt.protocol.types.network.Response
import com.github.kklisura.cdt.protocol.types.network.ResponseBody

WebUI.openBrowser('');

WebDriver driver = DriverFactory.getWebDriver();
ChromeDevToolsService service = CDTUtils.getService(driver);

Network network = service.getNetwork();

// For more details: https://chromedevtools.github.io/devtools-protocol/tot/Network/
network.onResponseReceived({def response -> 
    String requestId = response.getRequestId();
	Response res = response.getResponse();

	Integer status = res.getStatus();
	if (status == null)  {
		return;
	}

	String statusText = res.getStatusText();

	ResponseBody body = network.getResponseBody(requestId);
	String rawBody = body.getBody();

	if (status >= 200 && status < 300) {
		printf('[OK %d] %s\r\n', status, res.getUrl());
	} else {
		printf('[Error %d] %s\r\n', status, res.getUrl());
		if (StringUtils.isNotBlank(statusText)) {
			printf('-> Status text: %s\r\n', statusText);
		}
		if (StringUtils.isNotBlank(rawBody)) {
			printf('-> Body: %s\r\n', rawBody);
		}
	}
})

network.enable();


WebUI.navigateToUrl('https://www.atlassian.com/software/jira');




