package aplicatie_finala.L02_2_rate_limiter.rezolvare;

public class Response {
    private int responseId;
    private String clientName;

    public Response(int responseId, String clientName) {
        this.responseId = responseId;
        this.clientName = clientName;
    }

    public int getResponseId() {
        return responseId;
    }
    public String getClientName() {
        return clientName;
    }

    @Override
    public String toString() {
        return "Response{" + "Id=" + responseId + ", client=" + clientName + '}';
    }

}
