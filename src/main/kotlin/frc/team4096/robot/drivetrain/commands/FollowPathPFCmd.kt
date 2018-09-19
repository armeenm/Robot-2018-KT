package frc.team4096.robot.drivetrain.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4096.engine.motion.PFPath
import frc.team4096.engine.motion.PIDVAVals
import frc.team4096.robot.drivetrain.DriveConsts
import frc.team4096.robot.drivetrain.DriveSubsystem
import frc.team4096.robot.sensors.Gyro
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.followers.EncoderFollower

/**
 * Follows path using Jaci's EncoderFollower.
 *
 * @param path Path object to follow
 * @param pidvaVals PIDVA gains to configure follower with. I is ignored and A is ADDITIONAL gain.
 */
class FollowPathPFCmd(path: PFPath, pidvaVals: PIDVAVals = DriveConsts.PIDVA_GAINS_PF) : Command() {
    private val leftFollower = EncoderFollower(path.modifier!!.leftTrajectory)
    private val rightFollower = EncoderFollower(path.modifier!!.rightTrajectory)

    private var l = 0.0
    private var r = 0.0
    private var gyroHeading = 0.0
    private var desiredHeading = 0.0
    private var angleDifference = 0.0
    private var turn = 0.0

    init {
        isInterruptible = false
        requires(DriveSubsystem)

        listOf(leftFollower, rightFollower).forEach {
            it.configurePIDVA(pidvaVals.kP, pidvaVals.kI, pidvaVals.kD, pidvaVals.kV, pidvaVals.kA)
        }

        leftFollower.configureEncoder(
                DriveSubsystem.leftEncoder.get(),
                DriveConsts.ENCODER_TPR,
                DriveConsts.DT_WHEEL_DIAMETER
        )
        rightFollower.configureEncoder(
                DriveSubsystem.rightEncoder.get(),
                DriveConsts.ENCODER_TPR,
                DriveConsts.DT_WHEEL_DIAMETER
        )
    }

    override fun initialize() {
        DriveSubsystem.apply { leftEncoder.reset(); rightEncoder.reset() }
        Gyro.reset()
        leftFollower.reset()
        rightFollower.reset()
    }

    override fun execute() {
        // Distance
        l = leftFollower.calculate(DriveSubsystem.leftEncoder.get())
        r = rightFollower.calculate(DriveSubsystem.rightEncoder.get())
        // Heading
        gyroHeading = Gyro.angle
        desiredHeading = Pathfinder.r2d(leftFollower.heading)
        angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading)
        turn = 0.8 * (-1.0 / 80.0) * angleDifference

        DriveSubsystem.diffDrive.tankDrive(l + turn, r - turn)
    }

    override fun isFinished() = leftFollower.isFinished && rightFollower.isFinished

    override fun end() = DriveSubsystem.diffDrive.tankDrive(0.0, 0.0)
}