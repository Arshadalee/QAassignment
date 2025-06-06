import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;

public class RestCacheValidationTest {
    @Test
    public void test() {
        String baseUrl = "http://test01.p9m.net/";

        // Send GET request
        Response response = RestAssured
                .given()
                .when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .extract()
                .response();


        System.out.println("Response Headers:");
        response.getHeaders().forEach(header ->
                System.out.println(header.getName() + ": " + header.getValue())
        );

        // === Validate TTL ===
        String cacheControl = response.getHeader("Cache-Control");
        if (cacheControl != null && cacheControl.contains("max-age")) {
            System.out.println("TTL (max-age) found: " + cacheControl);
        } else {
            System.out.println("Cache-Control header with max-age not found.");
        }

        // === Validate Cache Mode ===

        if (cacheControl != null) {
            if (cacheControl.contains("public")) {
                System.out.println("Cache Mode: public");
            } else if (cacheControl.contains("private")) {
                System.out.println("Cache Mode: private");
            } else if (cacheControl.contains("no-store")) {
                System.out.println("Cache Mode: no-store");
            } else {
                System.out.println("Cache Mode not clearly defined.");
            }
        }
         //=== CDN-Specific Cache Headers ===
        String xCache = response.getHeader("X-Cache");
        String cfCache = response.getHeader("CF-Cache-Status");
        String via = response.getHeader("Via");

        if (xCache != null) {
            System.out.println("\nCDN X-Cache: " + xCache);
           assertThat("X-Cache should indicate HIT, MISS, or REFRESH",
                    xCache.toLowerCase(), // Convert to lowercase for case-insensitive matching
                    // CORRECTED LINE: Use Hamcrest's anyOf directly with containsString matchers
                    anyOf(containsString("hit"), containsString("miss"), containsString("refresh")));
        } else {
            System.out.println("\nX-Cache header not present.");
        }

        if (cfCache != null) {
            System.out.println("Cloudflare Cache Status (CF-Cache-Status): " + cfCache);
        }

        if (via != null) {
            System.out.println("Via Header (proxy evidence): " + via);
        }

        // === Surrogate / Purge Support Check ===
        String surrogateControl = response.getHeader("Surrogate-Control");
        if (surrogateControl != null) {
            System.out.println("Surrogate-Control: " + surrogateControl);
        }

        System.out.println("\n CDN and cache validation complete.");
    }
}
