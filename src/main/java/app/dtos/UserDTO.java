package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("parentName")
    private String parentName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("children")
    private HashSet<ChildDTO> children = new HashSet<>();

    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY, value="password")
    private String password;



}