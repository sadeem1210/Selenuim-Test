import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
 
public class amazonTest {

	public static void main(String[] args) {
		
		WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	 
		// open amazon
	     try {
	            // Step 1: Open Amazon.eg and login
	            driver.get("https://www.amazon.eg/");
	            driver.manage().window().maximize();
	            System.out.println(">>> Reached point 1");
	            // Click on Sign in
	            driver.findElement(By.id("nav-link-accountList")).click();
	            System.out.println(">>> Reached point 2");
	            
	            
	            // Fill login credentials (replace with your own or use input)
	            driver.findElement(By.id("ap_email_login")).sendKeys("ms-s14@hotmail.com");
	            
	            driver.findElement(By.id("continue")).click();
	            driver.findElement(By.id("ap_password")).sendKeys("****");
	            driver.findElement(By.id("signInSubmit")).click();

	            // Step 2: Open "All" menu
	            wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-hamburger-menu"))).click();
	            Thread.sleep(1000);

	            // Step 3: Click on "Video Games" → "All Video Games"
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='See all']"))).click();
	            
	            WebElement videoGames = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Video Games']")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", videoGames);

	            WebElement allVideoGames = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='All Video Games']")));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", allVideoGames);

	            // Step 4: Apply filters – Free Shipping and Condition New
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='All customers get FREE Shipping on orders shipped by Amazon']"))).click();
	            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='New']"))).click();

	            // Step 5: Sort by Price: High to Low
	            WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("a-autoid-0-announce")));
	            sortDropdown.click();
	            sortDropdown.findElement(By.xpath("//option[text()='Price: High to Low']")).click();

	            // Step 6: Add all products under 15,000 EGP
	            boolean hasMorePages = true;

	            while (hasMorePages) {
	                List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
	                    By.cssSelector(".s-main-slot .s-result-item[data-component-type='s-search-result']")));

	                for (WebElement item : items) {
	                    try {
	                        WebElement priceElement = item.findElement(By.xpath(".//span[contains(@class,'a-color-base') and contains(text(),'EGP')]"));
	                        String rawPrice = priceElement.getText();
	                        String cleanedPrice = rawPrice.replaceAll("[^\\d.]", "");
	                        double price = Double.parseDouble(cleanedPrice);

	                        System.out.println("💰 Product price: " + price);

	                        if (price < 15000.0) {
	                            // Open product in new tab
	                        	List<WebElement> links = item.findElements(By.cssSelector("a.a-link-normal.s-no-outline"));
	                        	if (links.isEmpty()) {
	                        	    System.out.println("⚠️ Skipping item: No product link found.");
	                        	    continue;
	                        	}
	                        	String itemUrl = links.get(0).getAttribute("href");
	                        	((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", itemUrl);
	                        	System.out.println("🆕 New tab opened");

	                        	// Switch to new tab
	                        	List<String> tabs = new ArrayList<>(driver.getWindowHandles());
	                        	driver.switchTo().window(tabs.get(tabs.size() - 1));
	                        	System.out.println("🔁 Switched to new tab");


	                        	try {
	                        	    // Find the element that displays the availability status
	                        	    WebElement availability = driver.findElement(By.id("availability_feature_div"));

	                        	    // Check if the text contains "Currently unavailable" in a case-insensitive manner
	                        	    if (availability.getText().toLowerCase().contains("currently unavailable")) {
	                        	        System.out.println("🚫 Item unavailable. Skipping.");
	                        	        
	                        	        // Close the current tab
	                        	        driver.close();
	                        	        
	                        	        // Switch back to the main window
	                        	        driver.switchTo().window(tabs.get(0));
	                        	        
	                        	        // Skip the current iteration and move on to the next item (do not add to cart)
	                        	        continue;  // Skips to the next item in the loop
	                        	    } else {
	                        	        // Item is available, proceed with adding to the cart
	                        	        System.out.println("Item is available. Proceeding to add to cart.");

	                        	        // Add to cart (this code will execute only if the item is available)
	                        	        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
	                        	        addToCartButton.click();

	                        	        // Optionally wait for confirmation of adding the item to the cart
	                        	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("huc-v2-order-row-confirm-text")));
	                        	        System.out.println("✔️ Item added to cart.");
	                        	    }
	                        	} catch (NoSuchElementException e) {
	                        	    // If the "availability" element is not found, assume the item is available
	                        	    System.out.println("Item available. Proceeding with adding to cart.");
	                        	    
	                        	    // Add to cart (this will be triggered if the availability element isn't found)
	                        	    WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
	                        	    addToCartButton.click();

	                        	    // Optionally wait for confirmation of adding the item to the cart
	                        	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("huc-v2-order-row-confirm-text")));
	                        	    System.out.println("✔️ Item added to cart.");
	                        	}


	                            try {
	                                WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
	                                addToCartBtn.click();
	                                System.out.println("✅ Added product to cart.");
	                            } catch (Exception e) {
	                                System.out.println("⚠️ Could not add to cart.");
	                            }

	                            driver.close();
	                            driver.switchTo().window(tabs.get(0));
	                        }
	                    } catch (Exception e) {
	                        System.out.println("❌ Skipping item: " + e.getMessage());
	                    }
	                }

	                // Try to go to the next page regardless
	                try {
	                    WebElement nextButton = driver.findElement(By.cssSelector(".s-pagination-next"));
	                    if (nextButton.isDisplayed()) {
	                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButton);
	                        Thread.sleep(3000);
	                    } else {
	                        hasMorePages = false;
	                    }
	                } catch (Exception e) {
	                    hasMorePages = false;
	                }
	            }


	            // Step 7: Verify items in cart
	            driver.findElement(By.id("nav-cart")).click();
	            List<WebElement> cartItems = driver.findElements(By.cssSelector(".sc-list-item"));
	            if (cartItems.size() == 0) {
	                System.out.println("Cart is empty!");
	            } else {
	                System.out.println("Items added to cart: " + cartItems.size());
	            }

	         // Step 8: Calculate total (without placing order)
	            WebElement subtotal = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            	    By.cssSelector("#sc-subtotal-amount-activecart .a-price-whole")));
	            String totalText = subtotal.getText().replace(",", "");
	            System.out.println("🧾 Cart total (excluding shipping): " + totalText + " EGP");

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // driver.quit(); // Optional: Close browser
	        }
	    }
	}
