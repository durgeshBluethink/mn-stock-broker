package com.bluethink.mn.broker;

import com.bluethink.mn.broker.data.InMemoryStore;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import net.datafaker.fileformats.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class SymbolsControllerTest {
    private static final Logger LOG= LoggerFactory.getLogger(SymbolsControllerTest.class);

    @Inject
    @Client("/symbols")
    HttpClient client;

    @Inject
    InMemoryStore inMemoryStore;
    @BeforeEach
    void setup(){
        inMemoryStore.initailizeWith(10);
    }


    @Test
    void symbolsEndpointReturnsListOfSymbol(){
        var response = client.toBlocking().exchange("/", JsonNode.class);
        assertEquals(HttpStatus.OK,response.getStatus());
        assertEquals(10,response.getBody().get().size());
    }
    @Test
    void symbolsEndpointReturnsTheCorrectSymbol(){
        var testSymbol = new Symbol("TEST");
        inMemoryStore.getSymbolMap().put(testSymbol.value(),testSymbol);
        var response = client.toBlocking().exchange("/"+testSymbol.value(), Symbol.class);
        assertEquals(HttpStatus.OK,response.getStatus());
        assertEquals(testSymbol,response.getBody().get());
    }
    @Test
    void symbolsEndpointReturnsListOfSymbolTakingQueryParameterIntoAccount(){
        var max10 = client.toBlocking().exchange("/filter?max=10", JsonNode.class);
        assertEquals(HttpStatus.OK,max10.getStatus());
        LOG.debug("Max:10:{}",max10.getBody().get().toString());
        assertEquals(10,max10.getBody().get().size());
    }

}
