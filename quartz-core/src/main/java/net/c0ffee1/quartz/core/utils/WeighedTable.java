package net.c0ffee1.quartz.core.utils;

import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class WeighedTable<T> {
    public HashMap<T, Double> map;
    public HashMap<Double, Integer> weightMap;
    public HashMap<T, String> rarityMap;
    private double totalWeight = 0;
    private SecureRandom secureRandom;

    public WeighedTable(){
        map = new LinkedHashMap<>();
        rarityMap = new HashMap<>();
        weightMap = new HashMap<>();
        secureRandom = new SecureRandom();
    }

    public void addItem(T itemStack, double weight){
        map.put(itemStack, weight);
        totalWeight += weight;
        weightMap.put(weight, weightMap.getOrDefault(weight, 0)+1);
    }

    public void calculateRarities(Function<Double, String> rarityCalculator){
        for(T itemStack : map.keySet()){
            double weight = map.get(itemStack);
            double chance = (weight / totalWeight*100) * weightMap.get(weight);
            rarityMap.put(itemStack, rarityCalculator.apply(chance));
        }
    }

    public double getRarityDouble(T item){
        double weight = map.get(item);
        return (weight / totalWeight*100) * weightMap.get(weight);
    }

    public String getRarity(T item){
        return rarityMap.get(item);
    }

    public T testChances(Player player, double times){
        T currentItem = null;
        HashMap<Double, Integer> timeMap = new HashMap<>();
        for(int i = 0; i < times; i++){
            double random = secureRandom.nextDouble() * totalWeight;
            for(Map.Entry<T, Double> item : map.entrySet()){
                double weight = item.getValue();
                random -= weight;
                if (random <= 0.0d) {
                    currentItem = item.getKey();
                    double chance = ((map.get(currentItem) / totalWeight) * 100) * weightMap.get(map.get(currentItem));
                    timeMap.put(chance, timeMap.getOrDefault(chance, 0)+1);
                    break;
                }
            }
        }
        player.sendMessage("--------------------------");
        for(Double d : timeMap.keySet()){
            player.sendMessage(d + ": " + ((timeMap.get(d) / times)*100));
            player.sendMessage("--------");
        }

        return currentItem;
    }

    public T getNextItem(){
        double random = secureRandom.nextDouble() * totalWeight;
        T currentItem = null;

        for(Map.Entry<T, Double> item : map.entrySet()){
            double weight = item.getValue();
            random -= weight;
            if (random <= 0.0d) {
                currentItem = item.getKey();
                break;
            }
        }
        return currentItem;
    }
}