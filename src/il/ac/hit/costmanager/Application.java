
package il.ac.hit.costmanager;

import il.ac.hit.costmanager.model.CostManagerException;
import il.ac.hit.costmanager.model.DerbyDBModel;
import il.ac.hit.costmanager.model.IModel;
import il.ac.hit.costmanager.view.IView;
import il.ac.hit.costmanager.view.View;
import il.ac.hit.costmanager.viewmodel.IViewModel;
import il.ac.hit.costmanager.viewmodel.ViewModel;

/**
 * This is the main method that start the app.
 * It's init the IModel,IView,IViewModel and connecting all the components with each other.
 */

public class Application {

    public static void main(String[] args) {

        //creating the application components
        IModel model = null;
        try {
            model = new DerbyDBModel();
        } catch (CostManagerException e) {
            e.printStackTrace();
        }
        IView view = new View();
        IViewModel vm = new ViewModel();

        //connecting the components with each other
        view.setViewModel(vm);
        vm.setModel(model);
        vm.setView(view);

    }
}
