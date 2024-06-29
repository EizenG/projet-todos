package sn.ept.git.seminaire.poc.demo;


public  class CurrencyConverter {

      private final CurrencyService  service;

      public CurrencyConverter(CurrencyService service) {
            this.service = service;
      }

      public  double convert(Currency from , Currency to, double value){
            return service.convert(from,to,value);
      }
}
