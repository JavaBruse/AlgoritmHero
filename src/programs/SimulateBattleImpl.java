package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать события боя. Используется после каждой атаки юнита

    public SimulateBattleImpl() {
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        List<Unit> playerAliveUnits = new ArrayList<>(playerArmy.getUnits()); // Создание списка живых юнитов игрока
        List<Unit> computerAliveUnits = new ArrayList<>(computerArmy.getUnits()); // Создание списка живых юнитов компьютера

        while (!playerAliveUnits.isEmpty() && !computerAliveUnits.isEmpty()) {
            performRound(playerAliveUnits); // Вызов метода для раунда игроков
            performRound(computerAliveUnits); // Вызов метода для раунда компьютера
            playerAliveUnits.removeIf(unit -> !unit.isAlive()); // Удаление мертвых юнитов игрока
            computerAliveUnits.removeIf(unit -> !unit.isAlive()); // Удаление мертвых юнитов компьютера
        }
    }

    private void performRound(List<Unit> attackers ) throws InterruptedException {
        attackers.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed()); // Сортировка атакующих юнитов по убыванию атаки

        for (Unit attacker : attackers) {
            if (!attacker.isAlive()) {
                continue; // Пропуск мертвых юнитов
            }

            Unit target = attacker.getProgram().attack(); // Выбор цели атаки
            if (target != null && target.isAlive()) {
                printBattleLog.printBattleLog(attacker, target); // Запись события боя
            }
        }
    }
}
