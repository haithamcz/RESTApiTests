package org.haitham.demoswagger;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 *
 *  @author Haitham Jassim
 *
 */
public class StepDefinition {

    private RestEndpointNamedValidate restEndpointNamedValidate = new RestEndpointNamedValidate();
    private RestEndpointNamedValidateBatch restEndpointNamedValidateBatch = new RestEndpointNamedValidateBatch();
    private ResourceFileManager resourceFileManager = new ResourceFileManager();

    @Given("Accessible Sample valid data payload (.+)$")
    public void accessibleSampleValidDataPayload(String payloadFile) {
        Boolean stepTestResult;
        try {
            stepTestResult = !resourceFileManager.readFileFromResource(payloadFile).isEmpty();
        } catch (Exception e) {
            System.out.println("[accessibleSampleValidDataPayload] Test exception: " + e.getMessage());
            stepTestResult = false;
        }
        Assert.assertTrue("Possible to access payload data file: " + payloadFile, stepTestResult);
    }

    @When("Send payload (.+) to validate service$")
    public void sendPayloadToValidateService(String payloadFile) {
        Boolean stepTestResult;
        try {
            restEndpointNamedValidate.postReguest(resourceFileManager.readFileFromResource(payloadFile));
            stepTestResult = true;
        } catch (Exception e) {
            System.out.println("[sendPayloadToValidateService] Test exception: " + e.getMessage());
            stepTestResult = false;
        }
        Assert.assertTrue(
                "Payload data has sent to Validate Service. Payload file: " + payloadFile, stepTestResult
        );
    }

    @Then("Get response HTTP return code (.+) from validate service$")
    public void getResponseHttpReturnCodeFromValidateService(int httpReturnCode) {
        Assert.assertTrue(
                "Http return code check. Expected: " + httpReturnCode + " Returns: "
                        + restEndpointNamedValidate.getResponseStatusCode(),
                restEndpointNamedValidate.validateResponseStatusCode(httpReturnCode)
        );
    }


    @And("Response message status is (.+) from validate service$")
    public void responseMessageIsStatusFromValidateService(String status)  {
        Assert.assertTrue(
                "Status check. Expected: " + status + " Returns: "
                        + restEndpointNamedValidate.getResponseBody(),
                restEndpointNamedValidate.validateResponseStatus(status)
        );
    }

    @And("Response message contains (.+)$")
    public void responseMessageContainMsg(String message) {
        Assert.assertTrue(
                "Message check. Expected to contain: " + message + " Returns: "
                        + restEndpointNamedValidate.getResponseBody(),
                restEndpointNamedValidate.validateResponseMessage(message)
        );
    }

    @When("Send payload (.+) to validate batch service$")
    public void sendPayloadToValidateBatchService(String payloadFile) {
        Boolean stepTestResult;
        try {
            restEndpointNamedValidateBatch.postReguest(resourceFileManager.readFileFromResource(payloadFile));
            stepTestResult = true;
        } catch (Exception e) {
            System.out.println("[sendPayloadToValidateService] Test exception: " + e.getMessage());
            stepTestResult = false;
        }
        Assert.assertTrue(
                "Payload data has sent to Validate Service. Payload file: " + payloadFile, stepTestResult
        );
    }

    @Then("Get response HTTP return code (.+) from validate batch service$")
    public void getResponseHttpReturnCodeFromValidateBatchService(int httpReturnCode) {
        Assert.assertTrue(
                "Http return code check. Expected: " + httpReturnCode + " Returns: "
                        + restEndpointNamedValidateBatch.getResponseStatusCode(),
                restEndpointNamedValidateBatch.validateResponseStatusCode(httpReturnCode)
        );
    }

    @And("Response message status should not contain (.+) from validate batch service$")
    public void responseMessageIsStatusFromValidateBatchService(String status)  {
        Assert.assertFalse(
                "Status check. Expected: " + status + " Returns: "
                        + restEndpointNamedValidateBatch.getResponseBody(),
                restEndpointNamedValidateBatch.validateBatchResponseStatus(status)
        );
    }

}
