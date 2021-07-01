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
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.util.CDTUtils
import com.kms.katalon.core.util.internal.Base64

import com.github.kklisura.cdt.protocol.commands.Fetch
import com.github.kklisura.cdt.protocol.commands.Network
import com.github.kklisura.cdt.protocol.commands.Page
import com.github.kklisura.cdt.protocol.types.fetch.HeaderEntry
import com.github.kklisura.cdt.protocol.types.network.ErrorReason
import com.github.kklisura.cdt.protocol.types.network.Request
import com.github.kklisura.cdt.services.ChromeDevToolsService
import com.github.kklisura.cdt.protocol.types.network.Response

WebUI.openBrowser('');

WebUI.delay(3)

WebDriver driver = DriverFactory.getWebDriver();
ChromeDevToolsService service = CDTUtils.getService(driver);

Page page = service.getPage();
Fetch fetch = service.getFetch();

// For More Details: https://chromedevtools.github.io/devtools-protocol/tot/Fetch/
fetch.onRequestPaused({def request ->
    String requestId = request.getRequestId();
	Request req = request.getRequest();
	String url = req.getUrl();

	printf('[Request] %s\r\n', url);

	// If you want to mock your request with an custom response
	if (url.contains('/image')) {
		fetch.failRequest(requestId, ErrorReason.ACCESS_DENIED);
	} else if (url.contains('/api')) {
		List<HeaderEntry> headers = new ArrayList();
		
		String dummyBody = Base64.encode('{ "message": "Hello" }')

		HeaderEntry contentType = new HeaderEntry();
		contentType.setName('content-type');
		contentType.setValue('application/json');

		headers.add(contentType);

		fetch.fulfillRequest(requestId, 200, headers, dummyBody, null);
	} else { // Or just let it go...
		fetch.continueRequest(requestId);
	}
})


fetch.enable();
//page.enable();


WebUI.navigateToUrl('https://www.atlassian.com/software/jira');




