package com.example.LeBonCoinp.Users;

public class UserDTO {
    
    // Records are useful since they automatically create getters, equals, hashCode, and toString methods.
    // We also use them to avoid exposing the entire User entity (which may contain sensitive information like passwords).

    public record UserResponseDTO(Long id, String username, String email) { 
    }
    

}
