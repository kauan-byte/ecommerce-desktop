package br.edu.tds.ecommerce;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {

   private static Cloudinary cloudinary;

   public static Cloudinary getCloudinary() {

       if (cloudinary == null) {

           Map<String, String> config =
                   new HashMap<>();

           config.put("cloud_name", "dx4rqlyn4");
           config.put("api_key", "672833876915425");
           config.put("api_secret", "18I0Iac83-3qPv6AhnRiPGK4sN8");

           cloudinary =
                   new Cloudinary(config);
       }

       return cloudinary;
   }
}
