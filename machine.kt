package machine
import java.util.*

val scanner = Scanner(System.`in`)

enum class COMMAND(val order: String) {
	ACTION("Write action (buy, fill, take, remaining, exit): "),
	BUY("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: "),
		DONE("I have enough resources, making you a coffee!\n"),
		ERROR("Sorry, not enough "),
		BACK("\n"),
	FILL(""),
		WATER("Write how many ml of water do you want to add: "),
		MILK("Write how many ml of milk do you want to add: "),
		BEANS("Write how many grams of coffee beans do you want to add: "),
		CUPS("Write how many disposable cups of coffee do you want to add: "),
	TAKE("I gave you \$"),
	REMAINING("The coffee machine has:\n"),
	CONTINUE(""),
	EXIT("bye!");
}

class machina(water: Int, milk: Int, beans: Int, cups: Int, money: Int) {
	var w = water; var m = milk; var b = beans; var c = cups; var d = money;
	var err = "";
	var add = "";
	var command = COMMAND.ACTION;

	fun input(): String = scanner.nextLine();

	fun buy(x: String): Boolean {
		fun reset(x: String) {
			c++;
			if (x == "1") {
				w += 250; b += 16; d -= 4;
			} else if (x == "2") {
				w += 350; b += 20; d -= 7; m += 75;
			} else {
				w += 200; b += 12; d -= 6; m += 100;
			}
		}
		c--;
		if (x == "1") {
			w -= 250; b -= 16; d += 4;
		} else if (x == "2") {
			w -= 350; b -= 20; d += 7; m -= 75;
		} else {
			w -= 200; b -= 12; d += 6; m -= 100;
		}
		err = if (w < 0) "water!" else if (b < 0) "coffee beans!" else if (m < 0) "milk!" else if (c < 0) "cups!" else "";
		if (err == "") return true;
		reset(x);
		return false;
	}
	
	fun puts(): Unit = print(command.order + add);

	fun gets(order: String): Boolean {
		when(command.name) {
			"ACTION", "BUY" -> {
				when(order) {
					"buy" -> command = COMMAND.BUY;
					"fill" -> command = COMMAND.FILL;
					"take" -> command = COMMAND.TAKE;
					"remaining" -> command = COMMAND.REMAINING;
					"1", "2", "3" -> command = if (buy(order)) COMMAND.DONE else COMMAND.ERROR;
					else -> command = if (command == COMMAND.ACTION) COMMAND.EXIT else COMMAND.BACK;
				}
			}
			"WATER" -> { w += order.toInt(); command = COMMAND.MILK; }
			"MILK" -> { m += order.toInt(); command = COMMAND.BEANS; }
			"BEANS" -> { b += order.toInt(); command = COMMAND.CUPS; }
			"CUPS" -> { c += order.toInt(); command = COMMAND.ACTION; }
		}
		return if (command == COMMAND.EXIT) false else true;
	}
	
	fun does(): Boolean {
		when(command.name) {
			"REMAINING" -> { add = "$w of water\n$m of milk\n$b of coffee beans\n$c of disposable cups\n$d of money\n"; command = COMMAND.CONTINUE }
			"DONE", "BACK", "CONTINUE" -> { add = ""; command = COMMAND.ACTION; }
			"ERROR" -> { add = err; command = COMMAND.CONTINUE; }
			"FILL" -> { command = COMMAND.WATER; }
			"TAKE" -> { add = "$d\n"; d = 0; command = COMMAND.CONTINUE; }
			else -> return false;
		}
		return true;
	}
}

val exMachina = machina(400, 540, 120, 9, 550);

fun main(args: Array<String>) {
	do {
		exMachina.puts();
	} while (exMachina.does() || exMachina.gets(exMachina.input()));
}
