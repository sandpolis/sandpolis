//****************************************************************************//
//                                                                            //
//                Copyright © 2015 - 2019 Subterranean Security               //
//                                                                            //
//  Licensed under the Apache License, Version 2.0 (the "License");           //
//  you may not use this file except in compliance with the License.          //
//  You may obtain a copy of the License at                                   //
//                                                                            //
//      http://www.apache.org/licenses/LICENSE-2.0                            //
//                                                                            //
//  Unless required by applicable law or agreed to in writing, software       //
//  distributed under the License is distributed on an "AS IS" BASIS,         //
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  //
//  See the License for the specific language governing permissions and       //
//  limitations under the License.                                            //
//                                                                            //
//****************************************************************************//
import Foundation
import SwiftProtobuf

// If the compiler emits an error on this type, it is because this file
// was generated by a version of the `protoc` Swift plug-in that is
// incompatible with the version of SwiftProtobuf to which you are linking.
// Please ensure that your are building against the same version of the API
// that was used to generate this file.
fileprivate struct _GeneratedWithProtocGenSwiftVersion: SwiftProtobuf.ProtobufAPIVersionCheck {
  struct _2: SwiftProtobuf.ProtobufAPIVersion_2 {}
  typealias Version = _2
}

///*
/// An enumeration of all Sandpolis instance types.
enum Util_Instance: SwiftProtobuf.Enum {
  typealias RawValue = Int
  case charcoal // = 0
  case server // = 1
  case client // = 2
  case viewer // = 3
  case installer // = 4
  case UNRECOGNIZED(Int)

  init() {
    self = .charcoal
  }

  init?(rawValue: Int) {
    switch rawValue {
    case 0: self = .charcoal
    case 1: self = .server
    case 2: self = .client
    case 3: self = .viewer
    case 4: self = .installer
    default: self = .UNRECOGNIZED(rawValue)
    }
  }

  var rawValue: Int {
    switch self {
    case .charcoal: return 0
    case .server: return 1
    case .client: return 2
    case .viewer: return 3
    case .installer: return 4
    case .UNRECOGNIZED(let i): return i
    }
  }

}

#if swift(>=4.2)

extension Util_Instance: CaseIterable {
  // The compiler won't synthesize support with the UNRECOGNIZED case.
  static var allCases: [Util_Instance] = [
    .charcoal,
    .server,
    .client,
    .viewer,
    .installer,
  ]
}

#endif  // swift(>=4.2)

///*
/// An enumeration of all official instance subtypes (codenames).
enum Util_InstanceFlavor: SwiftProtobuf.Enum {
  typealias RawValue = Int
  case none // = 0

  /// The original Java-based server instance
  case vanilla // = 1

  /// A heavy-weight cross-platform client instance
  case mega // = 2

  /// A light-weight native client instance
  case micro // = 3

  /// A terminal-only viewer instance
  case ascetic // = 4

  /// A JavaFX viewer instance
  case lifegem // = 5

  /// An Android viewer instance
  case soapstone // = 6

  /// An iOS viewer instance
  case lockstone // = 7
  case UNRECOGNIZED(Int)

  init() {
    self = .none
  }

  init?(rawValue: Int) {
    switch rawValue {
    case 0: self = .none
    case 1: self = .vanilla
    case 2: self = .mega
    case 3: self = .micro
    case 4: self = .ascetic
    case 5: self = .lifegem
    case 6: self = .soapstone
    case 7: self = .lockstone
    default: self = .UNRECOGNIZED(rawValue)
    }
  }

  var rawValue: Int {
    switch self {
    case .none: return 0
    case .vanilla: return 1
    case .mega: return 2
    case .micro: return 3
    case .ascetic: return 4
    case .lifegem: return 5
    case .soapstone: return 6
    case .lockstone: return 7
    case .UNRECOGNIZED(let i): return i
    }
  }

}

#if swift(>=4.2)

extension Util_InstanceFlavor: CaseIterable {
  // The compiler won't synthesize support with the UNRECOGNIZED case.
  static var allCases: [Util_InstanceFlavor] = [
    .none,
    .vanilla,
    .mega,
    .micro,
    .ascetic,
    .lifegem,
    .soapstone,
    .lockstone,
  ]
}

#endif  // swift(>=4.2)

///*
/// An enumeration of major CPU architecture families.
enum Util_Architecture: SwiftProtobuf.Enum {
  typealias RawValue = Int
  case x86 // = 0
  case x8664 // = 1
  static let amd64 = x8664
  static let ia64 = x8664
  case aarch64 // = 2
  case armv8 // = 3
  case armv7 // = 4
  case armv6 // = 5
  case armv5 // = 6
  case sparc // = 7
  case sparcv9 // = 8
  case mips64 // = 9
  case ppc // = 10
  case ppc64 // = 11
  case s390X // = 12
  case UNRECOGNIZED(Int)

  init() {
    self = .x86
  }

  init?(rawValue: Int) {
    switch rawValue {
    case 0: self = .x86
    case 1: self = .x8664
    case 2: self = .aarch64
    case 3: self = .armv8
    case 4: self = .armv7
    case 5: self = .armv6
    case 6: self = .armv5
    case 7: self = .sparc
    case 8: self = .sparcv9
    case 9: self = .mips64
    case 10: self = .ppc
    case 11: self = .ppc64
    case 12: self = .s390X
    default: self = .UNRECOGNIZED(rawValue)
    }
  }

  var rawValue: Int {
    switch self {
    case .x86: return 0
    case .x8664: return 1
    case .aarch64: return 2
    case .armv8: return 3
    case .armv7: return 4
    case .armv6: return 5
    case .armv5: return 6
    case .sparc: return 7
    case .sparcv9: return 8
    case .mips64: return 9
    case .ppc: return 10
    case .ppc64: return 11
    case .s390X: return 12
    case .UNRECOGNIZED(let i): return i
    }
  }

}

#if swift(>=4.2)

extension Util_Architecture: CaseIterable {
  // The compiler won't synthesize support with the UNRECOGNIZED case.
  static var allCases: [Util_Architecture] = [
    .x86,
    .x8664,
    .aarch64,
    .armv8,
    .armv7,
    .armv6,
    .armv5,
    .sparc,
    .sparcv9,
    .mips64,
    .ppc,
    .ppc64,
    .s390X,
  ]
}

#endif  // swift(>=4.2)

///*
/// An enumeration of major OS families.
enum Util_OsType: SwiftProtobuf.Enum {
  typealias RawValue = Int
  case linux // = 0
  case windows // = 1
  case macos // = 2
  static let osx = macos
  case ios // = 3
  case android // = 4
  case freebsd // = 5
  case openbsd // = 6
  case solaris // = 7
  case aix // = 8
  case UNRECOGNIZED(Int)

  init() {
    self = .linux
  }

  init?(rawValue: Int) {
    switch rawValue {
    case 0: self = .linux
    case 1: self = .windows
    case 2: self = .macos
    case 3: self = .ios
    case 4: self = .android
    case 5: self = .freebsd
    case 6: self = .openbsd
    case 7: self = .solaris
    case 8: self = .aix
    default: self = .UNRECOGNIZED(rawValue)
    }
  }

  var rawValue: Int {
    switch self {
    case .linux: return 0
    case .windows: return 1
    case .macos: return 2
    case .ios: return 3
    case .android: return 4
    case .freebsd: return 5
    case .openbsd: return 6
    case .solaris: return 7
    case .aix: return 8
    case .UNRECOGNIZED(let i): return i
    }
  }

}

#if swift(>=4.2)

extension Util_OsType: CaseIterable {
  // The compiler won't synthesize support with the UNRECOGNIZED case.
  static var allCases: [Util_OsType] = [
    .linux,
    .windows,
    .macos,
    .ios,
    .android,
    .freebsd,
    .openbsd,
    .solaris,
    .aix,
  ]
}

#endif  // swift(>=4.2)

// MARK: - Code below here is support for the SwiftProtobuf runtime.

extension Util_Instance: SwiftProtobuf._ProtoNameProviding {
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    0: .same(proto: "CHARCOAL"),
    1: .same(proto: "SERVER"),
    2: .same(proto: "CLIENT"),
    3: .same(proto: "VIEWER"),
    4: .same(proto: "INSTALLER"),
  ]
}

extension Util_InstanceFlavor: SwiftProtobuf._ProtoNameProviding {
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    0: .same(proto: "NONE"),
    1: .same(proto: "VANILLA"),
    2: .same(proto: "MEGA"),
    3: .same(proto: "MICRO"),
    4: .same(proto: "ASCETIC"),
    5: .same(proto: "LIFEGEM"),
    6: .same(proto: "SOAPSTONE"),
    7: .same(proto: "LOCKSTONE"),
  ]
}

extension Util_Architecture: SwiftProtobuf._ProtoNameProviding {
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    0: .same(proto: "X86"),
    1: .aliased(proto: "X86_64", aliases: ["AMD64", "IA_64"]),
    2: .same(proto: "AARCH64"),
    3: .same(proto: "ARMv8"),
    4: .same(proto: "ARMv7"),
    5: .same(proto: "ARMv6"),
    6: .same(proto: "ARMv5"),
    7: .same(proto: "SPARC"),
    8: .same(proto: "SPARCv9"),
    9: .same(proto: "MIPS64"),
    10: .same(proto: "PPC"),
    11: .same(proto: "PPC64"),
    12: .same(proto: "S390X"),
  ]
}

extension Util_OsType: SwiftProtobuf._ProtoNameProviding {
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    0: .same(proto: "LINUX"),
    1: .same(proto: "WINDOWS"),
    2: .aliased(proto: "MACOS", aliases: ["OSX"]),
    3: .same(proto: "IOS"),
    4: .same(proto: "ANDROID"),
    5: .same(proto: "FREEBSD"),
    6: .same(proto: "OPENBSD"),
    7: .same(proto: "SOLARIS"),
    8: .same(proto: "AIX"),
  ]
}
