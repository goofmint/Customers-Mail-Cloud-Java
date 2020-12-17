package customersmailcloud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class CustomersMailCloudException extends Exception {
    private ArrayList<String> messages;

    public CustomersMailCloudException() {
        
    }
    
    public CustomersMailCloudException(String msg) {
        super(msg);
    }
    
    public CustomersMailCloudException(Throwable cause) {
        super(cause);
    }
    
    public CustomersMailCloudException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CustomersMailCloudException(List<Map<String, String>> errors) {
        super(errors.get(0).get("message"));
        messages = new ArrayList<String>();
        for (Map<String, String> error: errors){
            messages.add(error.get("message"));
        }
    }
}
