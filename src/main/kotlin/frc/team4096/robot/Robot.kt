package frc.team4096.robot

import edu.wpi.first.wpilibj.*
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4096.robot.subsystems.*
import frc.team4096.robot.util.MiscConsts

object Robot: TimedRobot() {
	// Sensors
	val gyro = ADXRS450_Gyro()
	// Note: use the average value from the pressure sensor
	private val pressureSensor = AnalogInput(MiscConsts.AIN_PRESSURE)

	private val cameraServer = CameraServer.getInstance()
	private val subsystemList = listOf(DriveSubsystem, IntakeSubsystem, ElevatorSubsystem, ClimberSubsystem)

	override fun robotInit() {
		// Will implicitly run init method in each subsystem
		subsystemList
		// Put all of the subsystems on SmartDashboard
		subsystemList.map{ zSubsystem -> SmartDashboard.putData(zSubsystem) }

		// Miscellaneous setups
		gyro.reset()
		// 2 ^ 12 average bits
		pressureSensor.averageBits = 12

		cameraServer.startAutomaticCapture()
	}

	override fun disabledInit() {
		// Empty out the scheduler on disable
		Scheduler.getInstance().removeAll()
	}

	override fun disabledPeriodic() { }

	override fun autonomousInit() {
		// Reset all subsystems for autonomous
		subsystemList.map{ zSubsystem -> zSubsystem.autoReset() }
	}

	override fun autonomousPeriodic() { }

	override fun teleopInit() {
		// Reset all subsystems for teleop
		subsystemList.map{ zSubsystem -> zSubsystem.teleopReset() }
	}

	override fun teleopPeriodic() { }

	override fun testInit() { }

	override fun testPeriodic() { }
}
