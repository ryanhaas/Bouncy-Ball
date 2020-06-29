package resources;

public class Encrypter {
	private final String key1 = "p>eWAkGK-.o0uV\"Fi@ 3n+tx(yDd^B4TC%q5v!Q;\\w\'7$XY`#JH?h*Z,Iclb8O9zg:=M/fsUrRL6&)2<EmS~Pa1j_N";
	private final String key2 = "OG\"*=V`dQ<U 1Ks#&@l+IDruE>.\\Xg6kv/YTzJ-_Ai)7CptZ3nP9~L!:bS^W?%$H\'co08,hB(y;FfqaMwRx4e5mjN2";
	private final String key3 = "N~vG$2`?18z#Ld_KIB7Vt>;.59ZF*Dq(6H+WygUJu YlnT%^oSiEf!bsxQXr3=hw\"0,ReAjcmpPO&/\'-MC:4)ak@<\\";
	private final String key4 = "K~u3jIsYy,iTwfUV\'E+bq 2r:W(R%/BG?vO8Nn#x!$4\"mZ_p5^JQ7t6d);@1XLhPH9>eS\\0D=`o-MAaC<Fk*czl.&g";
	private final String[] keys = { key1, key2, key3, key4 };

	public static final int LOW_LEVEL_ENCRYPTION = 0;
	public static final int STANDARD_LEVEL_ENCRYPTION = 1;
	public static final int HIGH_LEVEL_ENCRYPTION = 2;
	public static final int MAX_LEVEL_ENCRYPTION = 3;

	private int initLevel;
	private int encLevel;
	private int decLevel;

	public Encrypter() {
		initLevel = STANDARD_LEVEL_ENCRYPTION;
		encLevel = 0;
		decLevel = initLevel;
	}

	public Encrypter(int level) {
		initLevel = level;
		encLevel = 0;
		decLevel = initLevel;
	}

	public String encrypt(String str) {
		String key = keys[encLevel];
		String encString = "";
		for (int x = 0; x < str.length(); x++) {
			String toAdd = Integer.toString(key.indexOf(str.charAt(x)));
			if (toAdd.length() != 2)
				toAdd = "0" + toAdd;
			encString += toAdd;
		}

		// Turns to different numbers, scrambling it more (scrambles beginning
		// less than the back
		String temp = "";
		for (int x = 0; x < encString.length(); x += 2) {
			int base = Integer.parseInt(encString.substring(x, x + 2));
			base += (x + 1) * 5;

			while (base > key.length() - 1)
				base -= key.length();
			String toAdd = Integer.toString(base);
			if (toAdd.length() != 2)
				toAdd = "0" + toAdd;
			temp += toAdd;
		}
		// Turns to combo of letters and numbers
		encString = temp;
		temp = "";
		for (int x = 0; x < encString.length(); x += 2) {
			int index = Integer.parseInt(encString.substring(x, x + 2));
			String toAdd = Character.toString(key.charAt(index));
			temp += toAdd;
		}
		encString = temp;

		if (encLevel < initLevel) {
			encLevel++;
			String toEncrypt = "";
			for (int x = 0; x < encLevel + 1; x++)
				toEncrypt += encString;
			return encrypt(toEncrypt);
		} else {
			encLevel = 0;
			return encString;
		}
	}

	public String decrypt(String str) {
		String key = keys[decLevel];
		String decString = "";
		// Turns letters and numbers to different numbers
		for (int x = 0; x < str.length(); x++) {
			String toAdd = Integer.toString(key.indexOf(str.charAt(x)));
			if (toAdd.length() != 2)
				toAdd = "0" + toAdd;
			decString += toAdd;
		}

		// Mid
		String temp = "";
		for (int x = 0; x < decString.length(); x += 2) {
			int base = Integer.parseInt(decString.substring(x, x + 2));
			base -= (x + 1) * 5;

			while (base < 0)
				base += key.length();
			String toAdd = Integer.toString(base);
			if (toAdd.length() != 2)
				toAdd = "0" + toAdd;
			temp += toAdd;
		}
		// Turns to combo of letters and numbers
		decString = temp;

		// End
		temp = "";
		for (int x = 0; x < decString.length(); x += 2) {
			int index = Integer.parseInt(decString.substring(x, x + 2));
			String toAdd = Character.toString(key.charAt(index));
			temp += toAdd;
		}
		decString = temp;

		if (decLevel > 0) {
			decLevel--;
			return decrypt(decString.substring(0, decString.length() / (decLevel + 2)));
		} else {
			decLevel = initLevel;
			return decString;
		}
	}
}
