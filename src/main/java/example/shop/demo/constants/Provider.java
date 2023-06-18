package example.shop.demo.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Provider {
    LOCAL("Local"),
    GOOGLE("Google"),

    GITHUB("GitHub");

    public final String value;
}
