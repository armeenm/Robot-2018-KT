package frc.team4096.robot.climber

import edu.wpi.first.wpilibj.Servo
import edu.wpi.first.wpilibj.VictorSP
import frc.team4096.engine.extensions.wpi.ZedSubsystem
import frc.team4096.robot.drivetrain.DriveSubsystem

/**
 * Climber subsystem.
 * Handles climber motor and release servo.
 */
object ClimberSubsystem : ZedSubsystem() {
    // Hardware
    private var motor = VictorSP(ClimberConsts.PWM_MOTOR).apply { inverted = ClimberConsts.MOTOR_INVERTED }
    private var servo = Servo(ClimberConsts.PWM_SERVO)

    // Hardware States
    var isReleased = false

    // Required Methods
    init {
        reset()
    }

    override fun reset() {
        isReleased = false
        servo.angle = 0.0
    }

    override fun log() {}

    override fun initDefaultCommand() {}

    override fun stop() {
        motor.speed = 0.0
    }

    /**
     * Release climbing apparatus.
     */
    fun release() {
        isReleased = true
        servo.angle = ClimberConsts.SERVO_RELEASE_ANGLE
    }

    var speed = 0.0
        set(speed) {
            if (isReleased) {
                motor.speed = speed
                field = speed
            }
        }
}