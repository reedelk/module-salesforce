package com.reedelk.salesforce.internal.http;

import org.apache.http.client.methods.HttpDelete;

import static com.reedelk.salesforce.internal.commons.Default.SALESFORCE_OBJECT_DELETE;

public class HttpRecordDelete extends HttpDelete implements HttpBaseRequest {

    public HttpRecordDelete(String instanceName, String objectName, String objectId) {
        super(String.format(SALESFORCE_OBJECT_DELETE, instanceName, objectName, objectId));
    }

    @Override
    public void setAccessToken(String accessToken) {
        HttpHeaders.addAuthorization(this, accessToken);
    }
}
