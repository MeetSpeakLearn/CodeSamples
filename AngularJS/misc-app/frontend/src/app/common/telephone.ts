export class Telephone {
    public static formatNorthAmerican(tel: string): string {
        var len: number = tel.length;
        switch (len) {
            case 10: return "(" + tel.substring(0, 3) + ") " + tel.substring(3, 6) + "-" + tel.substring(6, 10);
            case 7: return tel.substring(0, 3) + "-" + tel.substring(3, 7);
            default: return tel;
        }
    }
}
