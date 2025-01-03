package programs;


import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;
import java.util.stream.Collectors;


public class GeneratePresetImpl implements GeneratePreset {

    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int MAX_RETRY_COUNT = 100;

    @Override
    public Army generate(List<Unit> unitsAvailable, int maxPoints) {
        Army enemyArmy = new Army();
        Set<String> occupiedCoordinates = new HashSet<>();
        Map<String, Integer> unitTypeCount = new HashMap<>();

        // Предварительная сортировка юнитов по эффективности (жадный алгоритм)
        List<Unit> sortedUnits = unitsAvailable.stream()
                .sorted((unit1, unit2) -> {
                    double efficiency1 = (double) unit1.getBaseAttack() / unit1.getCost();
                    double efficiency2 = (double) unit2.getBaseAttack() / unit2.getCost();
                    if (Double.compare(efficiency2, efficiency1) != 0) {
                        return Double.compare(efficiency2, efficiency1);
                    } else {
                        double healthEfficiency1 = (double) unit1.getHealth() / unit1.getCost();
                        double healthEfficiency2 = (double) unit2.getHealth() / unit2.getCost();
                        return Double.compare(healthEfficiency2, healthEfficiency1);
                    }
                })
                .collect(Collectors.toList());

        int usedPoints = 0;

        for (Unit unit : sortedUnits) {
            String unitType = unit.getUnitType();
            int unitCost = unit.getCost();

            while (maxPoints >= unitCost && unitTypeCount.getOrDefault(unitType, 0) < MAX_UNITS_PER_TYPE) {
                int[] coordinates = findFreePoints(occupiedCoordinates);

                if (coordinates == null) {
                    break; // Нет доступных координат
                }

                // Создаем уникальный экземпляр юнита
                int currentCount = unitTypeCount.getOrDefault(unitType, 0) + 1;
                unitTypeCount.put(unitType, currentCount);
                String uniqueUnitName = unitType + " " + currentCount;
                Unit newUnit = new Unit(
                        uniqueUnitName,
                        unit.getUnitType(),
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        unit.getCost(),
                        unit.getAttackType(),
                        unit.getAttackBonuses(),
                        unit.getDefenceBonuses(),
                        coordinates[0],
                        coordinates[1]
                );

                // Добавляем юнита в армию
                enemyArmy.getUnits().add(newUnit);
                occupiedCoordinates.add(coordinates[0] + "," + coordinates[1]);
                maxPoints -= unitCost;
                usedPoints += unitCost;
            }
        }
        return enemyArmy;
    }

    private int[] findFreePoints(Set<String> occupiedCoordinates) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 21; y++) {
                String coordinateKey = x + "," + y;
                if (!occupiedCoordinates.contains(coordinateKey)) {
                    return new int[]{x, y};
                }
            }
        }
        return null; // Все координаты заняты
    }
}
