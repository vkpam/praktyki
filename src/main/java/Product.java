public class Product {

    private String name, unit;
    private int count, netPrice, vatRate;

    public void setName(String newName) {
        name = newName;
    }
    public String getName() {
        return name;
    }

    public void setCount(int newCount) {
        count = newCount;
    }
    public int getCount() {
        return count;
    }

    public void setUnit(String newUnit) {
        unit = newUnit;
    }
    public String getUnit() {
        return unit;
    }

    public void setNetPrice(int newNetPrice) {
        netPrice = newNetPrice;
    }
    public int getNetPrice() {
        return netPrice;
    }

    public int getNetValue() {
       return netPrice * count;
    }

    public void setVatRate(int newVatRate) {
        vatRate = newVatRate;
    }
    public int getVatRate() {
        return vatRate;
    }

    public int getVatValue() {
        return vatRate * getNetValue() / 100;
    }

    public int getGrossValue() {
        return getNetValue() + getVatValue();
    }
}