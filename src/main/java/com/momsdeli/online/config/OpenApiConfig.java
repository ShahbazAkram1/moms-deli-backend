/**
 * Author: Shahbaz Ali
 * Email: shahbazkhaniq@gmail.com
 * Date: 1/28/2024$
 * Time: 2:06 AM$
 * Project Name: moms_deli_backend$
 */

package com.momsdeli.online.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;


@OpenAPIDefinition(
        info = @Info(
                title = "Mom's Deli E-Commerce API",
                description = "RESTful API documentation for Mom's Deli E-Commerce Platform. This API provides endpoints for managing products, reviews, and user-related operations.",
                version = "v1",
                contact = @Contact(
                        name = "Shahbaz Ali",
                        email = "shahbazkhaniq@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public final class OpenApiConfig {

}
