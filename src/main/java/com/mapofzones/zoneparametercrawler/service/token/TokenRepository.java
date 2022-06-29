package com.mapofzones.zoneparametercrawler.service.token;

import com.mapofzones.zoneparametercrawler.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Token.TokenId> {

    @Query("FROM Token t WHERE LENGTH(t.tokenId.baseDenom) < 8")
    List<Token> findByTokenId_Zone(String zone);

}
