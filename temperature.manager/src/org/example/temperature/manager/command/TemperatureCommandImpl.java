package org.example.temperature.manager.command;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.example.temperature.manager.TemperatureManagerAdministration;

import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
//Create an instance of the component
@Instantiate(name = "temperature.manager.command")
//Use the handler command and declare the command as a command provider. The
//namespace is used to prevent name collision.
@CommandProvider(namespace = "temperature")
public class TemperatureCommandImpl {

  // Declare a dependency to a TemperatureAdministration service
  @Requires
  private TemperatureManagerAdministration m_administrationService;


  /**
   * Command implementation to express that the temperature is too high in the given room
   *
   * @param room the given room
   */

  // Each command should start with a @Command annotation
  @Command
  public void tempTooHigh(String room) {
	  
      m_administrationService.temperatureIsTooHigh(room);
      System.out.println("changement tem "+room);
  }

  @Command
  public void tempTooLow(String room){
	  m_administrationService.temperatureIsTooLow(room);
  }

}