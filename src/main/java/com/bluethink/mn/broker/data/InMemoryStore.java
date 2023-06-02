package com.bluethink.mn.broker.data;

import com.bluethink.mn.broker.Symbol;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Singleton
public class InMemoryStore {
    private static final Logger LOG=LoggerFactory.getLogger(InMemoryStore.class);
    private final Map<String, Symbol> symbolMap = new HashMap<>();
   private final Faker faker = new Faker();
    @PostConstruct
    public void initailize(){
        IntStream.range(0,10).forEach(i->
                addNewSymbol()
                );
    }
    public void initailizeWith(int numberOfEntries){
        symbolMap.clear();
        IntStream.range(0,numberOfEntries).forEach(i->
                addNewSymbol()
        );
    }

    private void addNewSymbol() {
        var symbol = new Symbol(faker.stock().nsdqSymbol());
        symbolMap.put(symbol.value(),symbol);
        LOG.debug("Added Symbol {}", symbol);
    }

    public Map<String, Symbol> getSymbolMap() {
        return symbolMap;
    }
}
