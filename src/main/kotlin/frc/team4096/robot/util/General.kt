package frc.team4096.robot.util

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.InstantCommand
import kotlin.math.abs

/*
"Commandify" any given lambda function, usually for subsystem calls.
Uses an inline anonymous function with a lambda as an argument (lambda has no parameters and returns Unit).
Creates an InstantCommand() object and overrides the initialize function with the given lambda.
An InstantCommand() is a command with isFinished set to true.
The lambda is crossinlined to prevent non-local returns which are used in InstantCommand.
 */
inline fun Commandify(crossinline method: () -> Unit): Command = object: InstantCommand() {
	override fun initialize() = method()
}

fun applyDeadband(inputValue: Double, deadBand: Double) = if (abs(inputValue) >= deadBand) inputValue else 0.0