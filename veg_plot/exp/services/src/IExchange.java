// source: examples\publish\IExchange.java
package examples.publish;

/**
 * Exchange rate methods
 */

public interface IExchange
  {
  /**
   * Return the exchange rate between two countries
   * @param country1 The country to convert from
   * @param country2 The country to convert to
   * @return The exchange rate
   */
  float getRate( String country1, String country2 );
  }