package scratch.ev3;

import java.rmi.RemoteException;

import lejos.remote.ev3.RMIRegulatedMotor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MotorController {

	private static final Logger L = LoggerFactory
			.getLogger(MotorController.class);

	@Autowired
	private MotorComposite motors;

	@RequestMapping("/connectMotor/{commandId}/{type}/{port}")
	public String connectMotor(@PathVariable("commandId") String commandId,
			@PathVariable("type") String type,
			@PathVariable("port") String port, Model model)
			throws RemoteException {
		L.info("connecting a {} motor on port {}: start", type, port);
		motors.createMotor(port, type, commandId);
		L.info("connecting a {} motor on port {}: done", type, port);
		return "ignored";
	}

	/**
	 * Normally Scratch should add a command id to the request, but for some
	 * reason when wait-command blocks are executed, Scratch just hangs. See
	 * http://scratch.mit.edu/discuss/topic/35231/?page=1#post-302258
	 * 
	 * @param type
	 * @param port
	 * @param model
	 * @return
	 * @throws RemoteException
	 */
	@RequestMapping("/connectMotor/{type}/{port}")
	public String connectMotorNoWait(@PathVariable("type") String type,
			@PathVariable("port") String port, Model model)
			throws RemoteException {
		L.info("connecting a {} motor on port {}: start", type, port);
		motors.createMotor(port, type);
		L.info("connecting a {} motor on port {}: done", type, port);
		return "ignored";
	}

	@RequestMapping("/speed/{port}/{speed}")
	public String setSpeed(@PathVariable("port") String port,
			@PathVariable("speed") int speed, Model model)
			throws RemoteException {
		RMIRegulatedMotor motor = motors.getMotor(port);
		if (motor == null) {
			L.error("motor on port {} is not connected", port);
		} else {
			motor.setSpeed(speed);
		}
		return "ignored";
	}

	@RequestMapping("/forward/{port}")
	public String forward(@PathVariable("port") String port, Model model)
			throws RemoteException {
		RMIRegulatedMotor motor = motors.getMotor(port);
		if (motor == null) {
			L.error("motor on port {} is not connected", port);
		} else {
			motor.forward();
		}
		return "ignored";
	}

	@RequestMapping("/backward/{port}")
	public String backward(@PathVariable("port") String port, Model model)
			throws RemoteException {
		RMIRegulatedMotor motor = motors.getMotor(port);
		if (motor == null) {
			L.error("motor on port {} is not connected", port);
		} else {
			motor.backward();
		}
		return "ignored";
	}

	@RequestMapping("/stop/{port}")
	public String stopImmediate(@PathVariable("port") String port)
			throws RemoteException {
		RMIRegulatedMotor motor = motors.getMotor(port);
		if (motor == null) {
			L.error("motor on port {} is not connected", port);
		} else {
			motor.stop(false);
		}
		return "ignored";
	}

	@RequestMapping("/stopImmediate/{port}")
	public String stop(@PathVariable("port") String port, Model model)
			throws RemoteException {
		RMIRegulatedMotor motor = motors.getMotor(port);
		if (motor == null) {
			L.error("motor on port {} is not connected", port);
		} else {
			motor.stop(true);
		}
		return "ignored";
	}

}