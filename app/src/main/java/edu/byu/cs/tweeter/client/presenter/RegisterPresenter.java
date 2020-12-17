package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterPresenter {
    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public RegisterPresenter(View view){
        this.view = view;
    }

    public RegisterResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {
        RegisterServiceProxy registerServiceProxy = getRegisterService();
        return registerServiceProxy.register(request);
    }

    public RegisterServiceProxy getRegisterService(){return new RegisterServiceProxy();}
}
