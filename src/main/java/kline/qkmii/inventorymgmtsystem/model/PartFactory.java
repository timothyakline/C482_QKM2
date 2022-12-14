/*
 * FNAM: PartFactory.java
 * DESC: Factory design pattern for creating parts
 * AUTH: Timothy Albert Kline
 * STRT: 18 Sep 2022
 * UPDT: 21 Sep 2022
 * VERS: 1.0
 * COPR: 2022 Timothy Albert Kline <timothyal.kline@gmail.com>
 */
package kline.qkmii.inventorymgmtsystem.model;

/**
 * A Factory pattern class that creates a part.
 * Three (3) overload methods are provided to create
 * a specific <code>Part</code> source type: InHouse or Outsourced.
 * @author Timothy Albert Kline
 * @version 1.0
 */
public class PartFactory {
  private static int partUID = -1;
  private boolean isNew;
  private int id;

  public enum PartSrcType {IN_HOUSE, OUTSOURCED}

  /**
   * Default constructor specifying a new product.
   * Assumes the part to be made has not been created yet.
   */
  public PartFactory() {
    isNew = true;
  }

  /**
   * Constructor specifying to copy information of a provided part.
   * Assumes given part passed is going to be "remade".
   * Stores the ID of the part.
   */
  public PartFactory(Part selectedPart) {
    isNew = false;
    id = selectedPart.getId();
  }

  /**
   * @return the next available UID
   */
  private int getPartUID() {
    return ++partUID;
  }

  /**
   * Given a part source, specifies information for a new part and creates it.
   * Determines what type of part to cast <code>srcValue</code> to the correct data type
   * for that part.
   *
   * @param source the source of the part, cannot be null
   * @param name the part name
   * @param price the part price
   * @param stock the part stock level
   * @param min the minimum stock level
   * @param max the maximum stock level
   * @param srcValue the machine code OR company name
   * @return the created part
   */
  public Part makePart(PartSrcType source, String name, double price, int stock, int min, int max, Object srcValue) {
    var id = (isNew) ? getPartUID() : this.id;
    if(isNew) {
      this.id = id;
      isNew = false;
    }

    return switch (source) {
      case IN_HOUSE -> new InHouse(id, name, price, stock, min, max, (Integer) srcValue);
      case OUTSOURCED -> new OutSourced(id, name, price, stock, min, max, (String) srcValue);
    };
  }

  /**
   * Specifies provided information for a new part made in-house and creates it.
   * Used exclusively for part creation during main application lifecycle
   *
   * @param name the part name
   * @param price the part price
   * @param stock the part stock level
   * @param min the minimum stock level
   * @param max the maximum stock level
   * @param machineID the machine code that made the part
   * @return an InHouse part
   */
  public InHouse makePart(String name, double price, int stock, int min, int max, int machineID) {
    return new InHouse(getPartUID(), name, price, stock, min, max, machineID);
  }

  /**
   * Specifies provided information for a new outsourced part and creates it.
   * Used exclusively for part creation during main application lifecycle.
   *
   * @param name the part name
   * @param price the part price
   * @param stock the part stock level
   * @param min the minimum stock level
   * @param max the maximum stock level
   * @param companyName the name of the company sourcing the part
   * @return an OutSourced part
   */
  public OutSourced makePart(String name, double price, int stock, int min, int max, String companyName) {
    return new OutSourced(getPartUID(), name, price, stock, min, max, companyName);
  }
}
