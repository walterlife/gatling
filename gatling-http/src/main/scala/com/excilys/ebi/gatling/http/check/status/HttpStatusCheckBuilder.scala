/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.http.check.status

import com.excilys.ebi.gatling.core.check.{CheckBuilderVerifyOne, CheckBuilderFind}
import com.excilys.ebi.gatling.core.check.{CheckBuilderVerify, CheckBuilderSave}
import com.excilys.ebi.gatling.core.session.Session
import com.excilys.ebi.gatling.core.util.StringHelper.EMPTY
import com.excilys.ebi.gatling.http.check.{HttpCheckBuilder, HttpCheck}
import com.excilys.ebi.gatling.http.request.HttpPhase.{StatusReceived, HttpPhase}

/**
 * HttpStatusCheckBuilder class companion
 *
 * It contains DSL definitions
 */
object HttpStatusCheckBuilder {
	/**
	 * Will check that the response status is in the specified range
	 *
	 * @param range the specified range
	 */
	def status = new HttpStatusCheckBuilder(CheckBuilderVerify.in, Nil, None) with CheckBuilderFind[HttpCheckBuilder[HttpStatusCheckBuilder]]
}

/**
 * This class builds a response status check
 *
 * @param to the optional session key in which the extracted value will be stored
 * @param strategy the strategy used to check
 * @param expected the expected value against which the extracted value will be checked
 */
class HttpStatusCheckBuilder(strategy: (List[String], List[String]) => Boolean, expected: List[Session => String], saveAs: Option[String])
		extends HttpCheckBuilder[HttpStatusCheckBuilder]((s: Session) => EMPTY, None, strategy, expected, saveAs, StatusReceived) {

	private[http] def newInstance(what: Session => String, occurrence: Option[Int], strategy: (List[String], List[String]) => Boolean, expected: List[Session => String], saveAs: Option[String], when: HttpPhase) =
		new HttpStatusCheckBuilder(strategy, expected, saveAs)

	private[gatling] def newInstanceWithFindOne(occurrence: Int) =
		new HttpStatusCheckBuilder(strategy, expected, saveAs) with CheckBuilderVerifyOne[HttpCheckBuilder[HttpStatusCheckBuilder]]

	private[gatling] def newInstanceWithFindAll = throw new UnsupportedOperationException("Status checks are single valued")

	private[gatling] def newInstanceWithVerify(strategy: (List[String], List[String]) => Boolean, expected: List[Session => String] = Nil) =
		new HttpStatusCheckBuilder(strategy, expected, saveAs) with CheckBuilderSave[HttpCheckBuilder[HttpStatusCheckBuilder]]

	private[gatling] def build: HttpCheck = new HttpStatusCheck(expected, saveAs)
}
