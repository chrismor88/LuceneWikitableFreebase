package bean;

import java.util.Arrays;
import java.util.List;

public class EntryKB {

	private String mid1;
	private String mid2;
	private String predicate;
	private String title1;
	private String title2;
	private List<String> types1;
	private List<String> types2;
	
	public EntryKB() {
	}

	public String getMid1() {
		return mid1;
	}

	public void setMid1(String mid1) {
		this.mid1 = mid1;
	}

	public String getMid2() {
		return mid2;
	}

	public void setMid2(String mid2) {
		this.mid2 = mid2;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String ne1) {
		this.title1 = ne1;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String ne2) {
		this.title2 = ne2;
	}

	public List<String> getTypes1() {
		return types1;
	}

	public void setTypes1(String types1) {
		this.types1 = Arrays.asList(types1.split(","));
	}

	public List<String> getTypes2() {
		return types2;
	}

	public void setTypes2(String types2) {
		this.types2 = Arrays.asList(types2.split(","));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mid1 == null) ? 0 : mid1.hashCode());
		result = prime * result + ((mid2 == null) ? 0 : mid2.hashCode());
		result = prime * result + ((title1 == null) ? 0 : title1.hashCode());
		result = prime * result + ((title2 == null) ? 0 : title2.hashCode());
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result + ((types2 == null) ? 0 : types2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntryKB other = (EntryKB) obj;
		if (mid1 == null) {
			if (other.mid1 != null)
				return false;
		} else if (!mid1.equals(other.mid1))
			return false;
		if (mid2 == null) {
			if (other.mid2 != null)
				return false;
		} else if (!mid2.equals(other.mid2))
			return false;
		if (title1 == null) {
			if (other.title1 != null)
				return false;
		} else if (!title1.equals(other.title1))
			return false;
		if (title2 == null) {
			if (other.title2 != null)
				return false;
		} else if (!title2.equals(other.title2))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		if (types2 == null) {
			if (other.types2 != null)
				return false;
		} else if (!types2.equals(other.types2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EntryKB [mid1=" + mid1 + ", mid2=" + mid2 + ", predicate=" + predicate + ", title1=" + title1
				+ ", title2=" + title2 + ", types1=" + types1 + ", types2=" + types2 + "]";
	}
	
	
	
}
