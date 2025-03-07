package interview.ui;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class CalculatorPage {

    public static final Target DIGIT_2 = Target.the("Number 2")
            .located(By.id("com.miui.calculator:id/digit_2"));

    public static final Target DIGIT_3 = Target.the("Number 3")
            .located(By.id("com.miui.calculator:id/digit_3"));

    public static final Target ADD_OPERATOR = Target.the("Addition Operator")
            .located(By.id("com.miui.calculator:id/op_add"));

    public static final Target EQUAL_BUTTON = Target.the("Equal button")
            .located(By.id("com.miui.calculator:id/eq"));

    public static final Target RESULT_FIELD = Target.the("Result field")
            .located(By.id("com.miui.calculator:id/result"));
}

