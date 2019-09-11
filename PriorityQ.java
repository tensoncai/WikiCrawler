package pa1Revised;

public class PriorityQ 
{
//	public static void main(String[] args)
//	{
//		PriorityQ q = new PriorityQ();
//	}
	
	private Node[] heap;
	private int counter = 1;
	
	PriorityQ()
	{
		heap = new Node[2];
	}
	
	public void add(String s, int p)
	{
		// if the heap array is full, expand the array
		if (counter == heap.length)
		{
			heap = expandHeap();
		}
		
		// make the new node
		Node n = new Node(s, p);
		
		// add new node to the end of the heap array
		heap[counter] = n;
		
		heapifyUp(heap, counter);
		counter++;
	}
	
	public String returnMax()
	{
		if (counter > 1)
		{
			return heap[1].getString();
		}
		else
		{
			return null;
		}
	}
	
	public String extractMax()
	{
		if (counter > 1)
		{
			String max = returnMax();
			
			// replace first element with the last element
			heap[1] = heap[counter - 1];
			counter--;
			
			heapifyDown(heap, 1);
			
			return max;
		}
		else
		{
			return null;
		}
	}
	
	public void remove(int i)
	{
		if (counter > 1 && i < counter)
		{
			heap[i] = heap[counter - 1];
			heapifyDown(heap, i);
			counter--;
		}
	}
	
	public void decrementPriority(int i, int k)
	{
		if (counter > 1 && (i > 0 && i < counter))
		{
			int newPriority = heap[i].getPriority() - k;
			heap[i].setPriority(newPriority);
			heapifyDown(heap, i);
		}
	}
	
	public int[] priorityArray()
	{
		if (counter > 1)
		{
			int[] B = new int[counter];
			for (int i = 1; i < B.length; i++)
			{
				B[i] = heap[i].getPriority();
			}
			
			return B;
		}
		else
		{
			return null;
		}
	}
	
	public int getKey(int i)
	{
		return heap[i].getPriority();
	}
	
	public boolean isEmpty()
	{
		if (counter - 1 == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getValue(int i)
	{
		return heap[i].getString();
	}
	
	private Node[] heapifyUp(Node[] h, int i)
	{
		// int i is an array index
		Node child = new Node(heap[i].getString(), heap[i].getPriority());
		
		if (i > 1)
		{
			int j = i / 2; // parent index
			Node parent = new Node(heap[j].getString(), heap[j].getPriority());
			
			if (heap[i].getPriority() > heap[j].getPriority()) // if child priority is greater than parent priority
			{
				// swap child with parent
				Node temp = new Node(parent.getString(), parent.getPriority());
				heap[j] = child;
				heap[i] = temp;
				
				heapifyUp(h, j);
			}
		}

		return h;
	}
	
	private Node[] heapifyDown(Node[] h, int i)
	{
		Node parent = heap[i];
		int j = 0;	// j is the index of the child the parent will possible swap with
		
		if (2 * i >= counter)
		{
			return h;
		}
		else if (2 * i + 1 < counter)	// a right child exists
		{
			Node leftChild = heap[i * 2];
			Node rightChild = heap[(i * 2) + 1];
			
			// find child with the larger priority and set j as the child's index
			if (leftChild.getPriority() > rightChild.getPriority())
			{
				j = i * 2;
			}
			else
			{
				j = i * 2 + 1;
			}
		}
		else if (2 * i + 1 == counter)	// only a left child exists
		{
			j = i * 2;
		}

		if (parent.getPriority() < heap[j].getPriority())
		{
			// swap
			Node temp = parent;
			heap[i] = heap[j];
			heap[j] = temp;	
			
			heapifyDown(h, j);
		}
		return h;
	}
	
	private Node[] expandHeap()
	{
		Node[] newHeap = new Node[heap.length * 2];
		for (int i = 0; i < heap.length; i++)
		{
			newHeap[i] = heap[i];
		}
		return newHeap;
	}
	
	public void printHeapElements()
	{
		for (int i = 0; i < counter; i++)
		{
			if (heap[i] == null)
			{
				System.out.println(heap[i]);
			}
			else
			{
				System.out.print(heap[i].getString() + " " + heap[i].getPriority());
				System.out.println();
			}
		}
	}
	
	public int getCounter()
	{
		return counter;
	}
}
