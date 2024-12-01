import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;


import java.util.List;
import java.util.Locale;

public class ProductList implements Page {

    public WebDriver driver;
    @FindBy(id = "navbarDropdown") WebElement dropDown;

    @FindBy(className = "dropdown-item") List<WebElement> menu;

    @FindBy(tagName = "tr") List<WebElement> elements;

    @FindBy(tagName = "td") List<WebElement> td;

    @FindBy(xpath = "//div[contains(@class, 'btn')]/child::button")
    WebElement btnAdd;

    @FindBy(id = "editForm") WebElement addingForm;

    @FindBy(id = "type") WebElement type;

    @FindBy(id = "exotic") WebElement exotic;

    @FindBy(id = "save") WebElement save;



    @Override
    public void init(final WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void openProductList() {
        dropDown.click();
        menu.get(0).click();
    }

    public boolean searchWordInTable(String word) {
        return elements.stream().anyMatch(
                s -> s.getText().contains(word));
    }

    public ProductList addProduct(ProductInfo prodInfo) {
        btnAdd.click();
        addingForm.findElement(By.id("name")).sendKeys(prodInfo.name);
        Select select = new Select(type);
        if (prodInfo.isFruit) select.selectByValue("FRUIT");
        else select.selectByValue("VEGETABLE");
        if (prodInfo.isExotic) exotic.click();
        save.click();
        return this;
    }

    public static class ProductInfo {
        public String name;
        public boolean isFruit;
        public boolean isExotic;
        Faker faker = new Faker(new Locale("ru"));

        // для генерации продукта "с нуля"
        public ProductInfo(boolean isFruit) {
            if (isFruit) this.name = faker.food().fruit();
            else this.name = faker.food().vegetable();

            this.isFruit = isFruit;
            this.isExotic = faker.bool().bool();
        }
        // для объекта по описанию из приложения
        public ProductInfo(String description){
            if(description.contains("Фрукт")) {
                this.name = description.substring(2, description.indexOf("Ф") - 1);
                this.isFruit = true;
            } else {
                this.name = description.substring(2, description.indexOf("О") - 1);
                this.isFruit = false;
            }
            this.isExotic = description.contains("true");
        }

        public ProductInfo() {
        }

        // для объекта по запросу SQL
        public ProductInfo(String name, String isFruit, int isExotic) {
            this.name = name;
            this.isFruit = isFruit.equals("FRUIT");
            if (isExotic == 1) this.isExotic = true;
        }

        public ProductInfo(String name, boolean isFruit, boolean isExotic) {
            this.name = name;
            this.isFruit = isFruit;
            this.isExotic = isExotic;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFruit(String fruit) {
            this.isFruit = fruit.equals("FRUIT");
        }

        public void setExotic(int exotic) {
            if (exotic == 1) this.isExotic = true;
        }
    }

    public String getLastProduct() {
        return elements.get(elements.size() - 1).getText();
    }
}
