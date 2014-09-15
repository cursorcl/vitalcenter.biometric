package cl.eos.validate.view;

import java.util.List;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import vitalcenter.osgi.persistence.models.IClientService;
import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.status.api.StatusEvent;
import cl.eos.interfaces.view.IView;
import cl.eos.interfaces.view.IViewContainer;
import cl.eos.validate.controller.RegisterBikeController;


/**
 * @author eosorio
 */
public class PnlRegisterBikes extends JFXPanel implements IView, IViewContainer
{
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Registro Bicicletas";

	private RegisterBikesViewController viewController;
    private RegisterBikeController controller;
    private Scene scene;
    private Pane myPane;
    private String title = TITLE;
    private IClientService service;
    

    public void init()
    {

        Platform.runLater( new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Platform.setImplicitExit(false);
                    FXMLLoader loader = new FXMLLoader( getClass().getResource( "/cl/eos/validate/view/registerbikes.fxml" ));
                    myPane = (Pane) loader.load();
                    viewController = loader.getController();
                    scene = new Scene( myPane );
                    setScene( scene );
                }
                catch ( Exception e )
                {
                }
            }
        } );
    }

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void addView(IView view) {
		
	}

	@Override
	public void removeView(IView view) {
		
	}

	@Override
	public List<IView> getViews() {
		return null;
	}

	@Override
	public void onSaving(IEntity entity) {
		
	}

	@Override
	public void onSaved(IEntity entity) {
		
	}

	@Override
	public void onDeleting(IEntity entity) {
		
	}

	@Override
	public void onDeleted(IEntity entity) {
		
	}

	@Override
	public void onSelected(IEntity entity) {
		
	}

	@Override
	public void setController(IController controller) {
		this.controller = (RegisterBikeController) controller;
	}

	@Override
	public IController getController() {
		return controller;
	}

	@Override
	public void onFound(IEntity entity) {
		
	}

	@Override
	public void onChangeStatus(StatusEvent status) {
	}

	public IClientService getService() {
		return service;
	}

	public void setService(IClientService service) {
		this.service = service;
	}
}
