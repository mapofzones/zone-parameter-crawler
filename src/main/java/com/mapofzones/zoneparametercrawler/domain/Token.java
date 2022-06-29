package com.mapofzones.zoneparametercrawler.domain;

import javassist.compiler.TokenId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "TOKENS")
public class Token {

    @EmbeddedId
    private Token.TokenId tokenId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class TokenId implements Serializable {

        @Column(name = "ZONE")
        private String zone;

        @Column(name = "BASE_DENOM")
        private String baseDenom;
    }

}
