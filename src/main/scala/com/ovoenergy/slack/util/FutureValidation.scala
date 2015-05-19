package com.ovoenergy.slack.util

import scala.concurrent.{ExecutionContext, Future}

import scalaz._
import scalaz.Scalaz._

class FutureValidation[F, S](private val inner: Future[Validation[F, S]]) {
  def map[T](fn: S => T)(implicit ec: ExecutionContext): FutureValidation[F, T] =
    FutureValidation(inner map { validation => validation map fn })

  def leftMap[G](fn: F => G)(implicit ec: ExecutionContext): FutureValidation[G, S] =
    FutureValidation(inner map { validation => validation leftMap fn })

  def flatMap[T](fn: S => FutureValidation[F, T])(implicit ec: ExecutionContext): FutureValidation[F, T] =
    FutureValidation {
      inner.flatMap {
        case Success(s) => fn(s).asFuture
        case Failure(f) => Future.successful(f.failure[T])
      }
    }

  def fold[T](fail: F => T = identity[F] _, succ: (S) => T = identity[S] _)(implicit ec: ExecutionContext): Future[T] =
    inner map { validation => validation fold (fail = fail, succ = succ) }

  def foreach[T](fn: S => T)(implicit ec: ExecutionContext): Unit = map(fn)

  def recover(fn: PartialFunction[Throwable, Validation[F, S]])(implicit ec: ExecutionContext): FutureValidation[F, S] =
    FutureValidation[F, S](inner.recover(fn))

  def orElse(x: => FutureValidation[F, S])(implicit ec: ExecutionContext): FutureValidation[F, S] =
    FutureValidation {
      inner.flatMap {
        case Success(s) => Future.successful(s.success)
        case Failure(f) => x.asFuture
      }
    }

  def asFuture: Future[Validation[F, S]] = inner
}

object FutureValidation {

  def apply[F, S](inner: Future[Validation[F, S]]) = new FutureValidation[F, S](inner)

  def apply[F, S](inner: Validation[F, S]): FutureValidation[F, S] = FutureValidation(Future.successful(inner))

  def success[F, S](s: S): FutureValidation[F, S] = FutureValidation(Validation.success(s))

  def failure[F, S](f: F): FutureValidation[F, S] = FutureValidation(Validation.failure(f))

  def flatten[F, T](list: List[Validation[F, T]])(implicit errorSemigroup: Semigroup[F]): Validation[F, List[T]] = {
    type L[S] = Validation[F, S]
    list.sequence[L, T]
  }

  def sequence[F, T](seq: List[FutureValidation[F, T]])(implicit ec: ExecutionContext, errorSemigroup: Semigroup[F]): FutureValidation[F, List[T]] =
    FutureValidation {
      Future.sequence(seq.map(_.asFuture)).map(x => flatten(x))
    }

  def traverse[F, T1, T2](seq: List[T1])(fn: T1 => FutureValidation[F, T2])(implicit ec: ExecutionContext, errorSemigroup: Semigroup[F]): FutureValidation[F, List[T2]] =
    FutureValidation {
      Future.traverse(seq)(x => fn(x).asFuture).map(x => flatten(x))
    }

  def zip[F, T1, T2](t1: FutureValidation[F, T1],
                     t2: FutureValidation[F, T2])(implicit ec: ExecutionContext): FutureValidation[F, (T1, T2)] =
    for {
      r1 <- t1
      r2 <- t2
    } yield (r1, r2)

  def zip[F, T1, T2, T3](t1: FutureValidation[F, T1],
                         t2: FutureValidation[F, T2],
                         t3: FutureValidation[F, T3])(implicit ec: ExecutionContext): FutureValidation[F, (T1, T2, T3)] =
    for {
      r1 <- t1
      r2 <- t2
      r3 <- t3
    } yield (r1, r2, r3)

  def zip[F, T1, T2, T3, T4](t1: FutureValidation[F, T1],
                             t2: FutureValidation[F, T2],
                             t3: FutureValidation[F, T3],
                             t4: FutureValidation[F, T4])(implicit ec: ExecutionContext): FutureValidation[F, (T1, T2, T3, T4)] =
    for {
      r1 <- t1
      r2 <- t2
      r3 <- t3
      r4 <- t4
    } yield (r1, r2, r3, r4)

  def zip[F, T1, T2, T3, T4, T5](t1: FutureValidation[F, T1],
                                 t2: FutureValidation[F, T2],
                                 t3: FutureValidation[F, T3],
                                 t4: FutureValidation[F, T4],
                                 t5: FutureValidation[F, T5])(implicit ec: ExecutionContext): FutureValidation[F, (T1, T2, T3, T4, T5)] =
    for {
      r1 <- t1
      r2 <- t2
      r3 <- t3
      r4 <- t4
      r5 <- t5
    } yield (r1, r2, r3, r4, r5)
}