package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    // Метод для поиска подходящих юнитов для атаки
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {

        List<Unit> suitableUnits = new ArrayList<>();  // Список для подходящих юнитов
        HashSet<Integer> occupiedYCoordinates = new HashSet<>();  // Хэшсет для отслеживания занятых координат Y

        int numRows = unitsByRow.size();  // Общее количество строк (рядов) юнитов
        int rowDirection = isLeftArmyTarget ? -1 : 1;  // Направление по строкам: влево или вправо

        // Цикл для каждого ряда (строки)
        IntStream.range(0, numRows).forEach(rowIndex -> {
            for (Unit unit : unitsByRow.get(rowIndex)) {  // Проход по всем юнитам в текущем ряду
                if (unit.isAlive()) {  // Проверка, жив ли юнит
                    int targetRowIndex = rowIndex + rowDirection;  // Индекс целевого ряда (в зависимости от направления)
                    if (targetRowIndex >= 0 && targetRowIndex < numRows) {
                        // Проверка, занята ли ячейка в целевом ряду
                        boolean isOccupied = unitsByRow.get(targetRowIndex).stream()
                                .anyMatch(u -> u.getyCoordinate() == unit.getyCoordinate() && u.isAlive());
                        if (!isOccupied) {  // Если ячейка не занята, добавляем юнита
                            suitableUnits.add(unit);
                        }
                    } else {
                        suitableUnits.add(unit);  // Если целевого ряда нет, добавляем юнита в список
                    }
                }
            }
        });

        return suitableUnits;  // Возвращаем список подходящих юнитов
    }
}
