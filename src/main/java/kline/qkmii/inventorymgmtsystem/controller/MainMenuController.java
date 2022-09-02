package kline.qkmii.inventorymgmtsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import kline.qkmii.inventorymgmtsystem.model.Inventory;
import kline.qkmii.inventorymgmtsystem.model.Part;
import kline.qkmii.inventorymgmtsystem.model.Product;
import kline.qkmii.inventorymgmtsystem.util.FilePath;
import kline.qkmii.inventorymgmtsystem.util.SceneManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainMenuController implements Initializable {

    private SceneManager sceneManager;

    @FXML
    private Button menuExitBtn;

    @FXML
    private TableColumn<Part, Integer> partIDCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partUnitCol;

    @FXML
    private Button partsAddBtn;

    @FXML
    private Button partsDeleteBtn;

    @FXML
    private Button partsModifyBtn;

    //@FXML private TextField partsQueryTF;

    //@FXML private TableView<Part> partsTBLV;

    @FXML
    private Button prodAddBtn;

    @FXML
    private Button prodDeleteBtn;

    //@FXML private TableColumn<Product, Integer> prodIDCol;

    //@FXML private TableColumn<Product, Integer> prodInvCol;

    @FXML
    private Button prodModifyBtn;

    //@FXML private TableColumn<Product, String> prodNameCol;

    //@FXML private TextField prodQueryTF;

    //@FXML private TableView<Product> prodTBLV;

    //@FXML private TableColumn<Product, Double> prodUnitCol;

    public MainMenuController() {
        this.sceneManager = new SceneManager() {};
        System.out.println("Main Menu controller created");
    }

    @FXML
    void handleExitBtnEvent(ActionEvent ignoredEvent) {
        System.exit(0);
    }

    ///PARTS
    @FXML
    void handlePartsAddBtnEvent(ActionEvent event) throws IOException {
        sceneManager.switchScene(event, FilePath.ADD_PARTS_SCENE);
    }

    @FXML
    void handlePartsModBtnEvent(ActionEvent event) throws IOException {
        var fxmlLoader = sceneManager.loadScene(FilePath.MODIFY_PARTS_SCENE);

        ModifyPartController MPMController = fxmlLoader.getController();
        //TODO: DIALOG FOR WHEN A PART IS NOT SELECTED.
        MPMController.fetchPart(partsTblController.getDatabase().getSelectionModel().getSelectedItem());

        sceneManager.switchScene(event, fxmlLoader);
    }

    @FXML
    void handlePartsDelBtnEvent(ActionEvent ignoredEvent) {
        //TODO: Remove selected item from TableView
        //      Return dialogue if no highlighted item is focused on.
        //      If selection exists, prompt confirmation dialogue.
        //          - Delete item
        //TODO: Dialog if no item is selected.
        var selectedPart = partsTblController.getDatabase().getSelectionModel().getSelectedItem();
        if(Inventory.deletePart(selectedPart)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
        }
    }

//    @FXML
//    void handlePartsQueryInput(KeyEvent event) {
//        var query = partsTblController.getSearchQuery();
//        if(event.getCode() == KeyCode.ENTER) {
//            partsTblController.setDatabase(Inventory.lookupPart(query));
//            System.out.println("Part query received");
//        }
//    }

    ///PRODUCTS
    @FXML
    void handleProdAddBtnEvent(ActionEvent event) throws IOException {
        sceneManager.switchScene(event, FilePath.ADD_PRODUCT_SCENE);
    }

    @FXML
    void handleProdDelBtnEvent(ActionEvent ignoredEvent) {
        //TODO: Remove selected item from TableView
        //      A focused item on the TableView must exist
        //      Return dialogue if no highlighted item is focused on.
        //      If selection exists, prompt confirmation dialogue.
        //          - Delete item
        //TODO: DIALOG if item not selected.
        var selectedProduct = productTblController.getDatabase().getSelectionModel().getSelectedItem();
        if(Inventory.deleteProduct(selectedProduct)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleProdModBtnEvent(ActionEvent event) throws IOException {
        //TODO: create State Machine for Add/Modify
        //      - Change Labels & methods
        var fxmlLoader = sceneManager.loadScene(FilePath.MODIFY_PRODUCT_SCENE);

        ModifyProductController MPMController = fxmlLoader.getController();
        //TODO: DIALOG FOR WHEN A PART IS NOT SELECTED.
        MPMController.fetchProduct(productTblController.getDatabase().getSelectionModel().getSelectedItem());

        sceneManager.switchScene(event, fxmlLoader);
//        int index = -1;
//
//        for (var currentPart : Inventory.getAllParts()){
//            ++index;
//            if(currentPart.getId() == id) {
//                Inventory.getAllParts().set(index, part);
//                return true;
//            }
//        }
//        return false;
    }

//    @FXML
//    void handleProdQueryInput(KeyEvent event) {
//        //TODO: Verify data type of query (int/String).
//        var query = productTblController.getSearchQuery();
//        if(event.getCode() == KeyCode.ENTER) {
//            productTblController.setDatabase(Inventory.lookupProduct(query));
//            System.out.println("Part query received");
//        }
//    }

    public boolean searchPart(int id) {
        var foundPart = false;
        for (var part : Inventory.getAllParts()) {
            foundPart = part.getId() == id;
            if(foundPart) {
                break;
            }
        }

        return foundPart;
    }

    public boolean updateProduct(int id, Product product) {
        int index = -1;

        for (var currentProduct : Inventory.getAllProducts()){
            ++index;
            if(currentProduct.getId() == id) {
                Inventory.getAllProducts().set(index, product);
                return true;
            }
        }
        return false;
    }

    public Part selectPart(int id) {
        for(var part : Inventory.getAllParts()){
            if(part.getId() == id) {
                return part;
            }
        }
        return null;
    }

    @FXML private VBox partsTbl;
    @FXML private VBox productTbl;
    @FXML private DBTableController<Part> partsTblController = new DBTableController<>();
    @FXML private DBTableController<Product> productTblController = new DBTableController<>();

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTblController.setTableLabel("Parts");
        productTblController.setTableLabel("Products");

        partsTblController.setDatabase(Inventory.getAllParts());
        productTblController.setDatabase(Inventory.getAllProducts());

        partsTblController.populateTable();
        productTblController.populateTable();

        System.out.println("Main Menu initialized.");
    }
}