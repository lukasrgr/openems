import { Edge } from "../../edge/edge";
import { JsonrpcResponseSuccess } from "../base";


/**
 * Wraps a JSON-RPC Response for a GetEdgesRequest.
 * 
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "id": UUID,
 *   "result": {
 *      "edges": Edge[]
 *    }
 * }
 * </pre>
 */
export class GetEdgesResponse extends JsonrpcResponseSuccess {

    public constructor(
        public readonly id: string,
        public readonly result: {
            edges: Edge[]
        }
    ) {
        super(id, result);
    }
}