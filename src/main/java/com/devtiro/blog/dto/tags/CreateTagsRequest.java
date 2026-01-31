package com.devtiro.blog.dto.tags;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagsRequest {

    @NotEmpty(message = "At least one tag Name is required")
    @Size(max = 10, message = "Maximum {max} characters allowed")
    private Set<
            @Size(min = 2, max = 30, message = "Category name must be between {min} and {max} characters")
            @Pattern(regexp = "^[\\w\\s-]+$", message = "Category name can only contain letters, numbers, spaces, and dashes")
            String> names;

}
