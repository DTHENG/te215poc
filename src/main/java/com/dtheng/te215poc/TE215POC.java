package com.dtheng.te215poc;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import lombok.extern.slf4j.Slf4j;

/**
 * TE215 Soil Moisture Sensor Proof Of Concept
 *
 * @author Daniel Thengvall <fender5289@gmail.com>
 */
@Slf4j
public class TE215POC {

    /**
     * Connects with the TE215 Soil Moisture Sensor and establishes
     * both a digital and analog connection to the sensor. Once the
     * connection has been established listeners are added and the
     * changes in soil moisture will be logged by the example
     *
     * For more info look at the README.md in the root directory
     */
    public static void main(String args[]) throws Exception {

        log.info("Starting...");

        // Create gpio controller
        GpioController controller = GpioFactory.getInstance();

        // Create custom ADS1115 GPIO provider
        ADS1115GpioProvider analogProvider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);

        // Provision gpio digital input pin
        GpioPinDigitalInput digitalInput = controller.provisionDigitalInputPin(RaspiPin.GPIO_00);

        // Add listener to log the results of the digital input
        digitalInput.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                log.info("digitalInput event.getState() {}", event.getState());
            }
        });

        // Provision gpio analog input pins from ADS1115
        GpioPinAnalogInput analogInput = controller.provisionAnalogInputPin(analogProvider, ADS1115Pin.INPUT_A0);

        // Add listener to log the results of the analog input
        analogInput.addListener(new GpioPinListenerAnalog() {

            @Override
            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
                log.info("analogInput event.getValue() {}", event.getValue());
            }
        });


        Thread.sleep(1000 * 60 * 60 * 3); // 3 hours
    }
}
