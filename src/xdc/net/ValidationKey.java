package xdc.net;

class ValidationKey {

    /**
     * key[x]= ns(lock[x]^lock[x-1])
     *   ns is a nibble swap (switch the upper 4 bits with the lower 4 bits)
     * exception:
     * key[0] is a bit different
     * let's name A and B the 2 last bytes of the lock
     * key[0]= ns(lock[0]^A^B^0x05)         ; 0x05 is a kind of magic nibble
     */
    public static String getValidationKeyFromLock(String validationString) {
        StringBuffer key = new StringBuffer();
        int i;
        int u,l,o;
        int v;
        int lockLength = validationString.length();

        /* first byte is a special case */
        u = (int) validationString.charAt(0);
        l = (int) validationString.charAt(lockLength - 1);
        o = (int) validationString.charAt(lockLength - 2);

        /* do xor */
        u = u ^ l ^ o ^ 0x05;		/* don't forget the magic 0x5 */

        /* do nibble swap */
        v = (((u << 8) | u) >> 4) & 255;
        key.append((char) v);

        /* all other bytes use the same code */
        for (i = 1; i < lockLength; i++) {
            u = (int) validationString.charAt(i);
            l = (int) validationString.charAt(i - 1);

            /* do xor */
            u = u ^ l;

            /* do nibble swap */
            v = (((u << 8) | u) >> 4) & 255;

            key.append((char) v);
        }
        return escapeValidationKey(key.toString());
    }

    /**
     * Some characters are reserved, therefore we escape them.
     */
    private static String escapeValidationKey(String key) {
        StringBuffer safeKey = new StringBuffer();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);

            switch (c) {
                case 124:
                case 36:
                case 126:
                case 96:
                case 5:
                case 0:
                    safeKey.append(escapeChar(c));
                    break;

                default:
                    safeKey.append(c);
            }
        }
        return safeKey.toString();
    }

    /**
     * Escape one char
     */
    private static String escapeChar(char c) {
        String chr = "" + (int) c;
        while (chr.length() < 3) {
            chr = "0" + chr;
        }

        return "/%DCN" + chr + "%/";
    }
}
