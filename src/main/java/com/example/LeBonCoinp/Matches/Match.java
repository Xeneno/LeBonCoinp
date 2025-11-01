package com.example.LeBonCoinp.Matches;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "matches")
public class Match {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Long userId;
private Long itemId;
private Timestamp matchedAt;
private String status;

public Long getId() {
    return id;
}
public void setId(Long id) {
    this.id = id;
}
public Long getUserId() {
    return userId;
}
public void setUserId(Long userId) {
    this.userId = userId;
}
public Long getItemId() {
    return itemId;
}
public void setItemId(Long itemId) {
    this.itemId = itemId;
}
public Timestamp getMatchedAt() {
    return matchedAt;
}
public void setMatchedAt(Timestamp matchedAt) {
    this.matchedAt = matchedAt;
}
public String getStatus() {
    return status;
}
public void setStatus(String status) {
    this.status = status;
}

}
