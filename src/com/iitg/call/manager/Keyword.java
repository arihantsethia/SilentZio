/*************** Copyright 2012 AsyncTech ******************************

This file is part of SilentZio.

AsyncTech is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

AsyncTech is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with AsyncTech.  If not, see <http://www.gnu.org/licenses/>.
 ****************************************************************************/



package com.iitg.call.manager;

public class Keyword {

	int _id;
	String _keyword;

	// constructors

	public Keyword() {

	}

	public Keyword(String keyword) {

		this._id = 0;
		this._keyword = keyword;

	}

	public Keyword(int id, String keyword) {

		this._id = id;
		this._keyword = keyword;

	}

	// properties

	public int getID() {
		return this._id;
	}

	public void setID(int value) {
		this._id = value;
	}

	public String getKeyword() {
		return this._keyword;
	}

	public void setKeyword(String value) {
		this._keyword = value;
	}
}
